package challenge_summer.bigtraffic;

import challenge_summer.bigtraffic.domain.Event;
import challenge_summer.bigtraffic.domain.Member;
import challenge_summer.bigtraffic.domain.Schedule;
import challenge_summer.bigtraffic.domain.ScheduleSeat;
import challenge_summer.bigtraffic.domain.Seat;
import challenge_summer.bigtraffic.domain.Seat_hold;
import challenge_summer.bigtraffic.domain.Status;
import challenge_summer.bigtraffic.repository.EventRepository;
import challenge_summer.bigtraffic.repository.MemberRepository;
import challenge_summer.bigtraffic.repository.ScheduleRepository;
import challenge_summer.bigtraffic.repository.ScheduleSeatRepository;
import challenge_summer.bigtraffic.repository.SeatRepository;
import challenge_summer.bigtraffic.repository.Seat_holdRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PessimisticLockWaitTest {

    private static final long LOCK_HOLD_MILLIS = 2_000L; // 2초

    @Autowired
    TransactionTemplate transactionTemplate;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    ScheduleSeatRepository scheduleSeatRepository;
    @Autowired
    Seat_holdRepository seatHoldRepository;

    @Test
    void 같은_SeatHold의_비관적_락이_풀릴_때까지_다른_트랜잭션은_기다린다() throws Exception {
        Long seatHoldId = createSeatHold();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch firstLockAcquired = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(2);
        AtomicLong secondWaitMillis = new AtomicLong();
        Queue<Throwable> failures = new ConcurrentLinkedQueue<>();

        executorService.submit(() -> { //쓰레드하나
            try {
                transactionTemplate.executeWithoutResult(status -> {
                    seatHoldRepository.findByIdWithPessimisticLock(seatHoldId)
                            .orElseThrow();

                    firstLockAcquired.countDown();
                    sleepWhileHoldingLock(LOCK_HOLD_MILLIS);// 2초, 내부 함수
                });// -> 커밋
            } catch (Throwable e) {
                failures.add(e);
                firstLockAcquired.countDown();
            } finally {
                doneLatch.countDown();
            }
        });

        executorService.submit(() -> { // 쓰레드 둘
            try {
                firstLockAcquired.await(); //A가 먼저 실행하기위해서 일단 대기
                long startNanos = System.nanoTime();

                transactionTemplate.executeWithoutResult(status ->
                        seatHoldRepository.findByIdWithPessimisticLock(seatHoldId)
                                .orElseThrow()
                );

                secondWaitMillis.set(
                        TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos)
                );
            } catch (Throwable e) {
                failures.add(e);
            } finally {
                doneLatch.countDown();
            }
        });

        boolean completed = doneLatch.await(10, TimeUnit.SECONDS);
        executorService.shutdownNow();

        System.out.println("두 번째 트랜잭션의 락 대기 시간 = "
                + secondWaitMillis.get() + "ms");

        assertTrue(completed, "두 작업이 제한 시간 안에 끝나야 합니다.");
        assertEquals(0, failures.size(), "작업 중 예외가 없어야 합니다.");
        assertTrue(
                secondWaitMillis.get() >= 1_500L,
                "두 번째 트랜잭션은 첫 번째 트랜잭션의 락 해제를 기다려야 합니다."
        );
    }

    private Long createSeatHold() {
        return transactionTemplate.execute(status -> {
            Member member = new Member("락 대기 테스트 회원");
            memberRepository.createMember(member);

            Event event = new Event("락 대기 테스트 이벤트");
            eventRepository.CreateEvent(event);

            Seat seat = new Seat("LOCK-A1");
            seatRepository.create(seat);

            Schedule schedule = new Schedule(event, LocalDateTime.now().plusDays(1));
            scheduleRepository.save(schedule);

            ScheduleSeat scheduleSeat = new ScheduleSeat(seat, schedule, Status.HELD);
            scheduleSeatRepository.create(scheduleSeat);

            Seat_hold seatHold = new Seat_hold(
                    scheduleSeat,
                    member,
                    LocalDateTime.now().plusMinutes(5)
            );
            seatHoldRepository.create(seatHold);

            return seatHold.getId();
        });
    }

    private void sleepWhileHoldingLock(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("락 보유 스레드가 중단되었습니다.", e);
        }
    }
}
