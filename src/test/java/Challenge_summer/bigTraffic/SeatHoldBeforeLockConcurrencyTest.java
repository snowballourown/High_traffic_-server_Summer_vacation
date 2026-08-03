package challenge_summer.bigtraffic;


import challenge_summer.bigtraffic.domain.*;
import challenge_summer.bigtraffic.repository.*;
import challenge_summer.bigtraffic.service.Seat_holdService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@ActiveProfiles("mysqltest")
@SpringBootTest
public class SeatHoldBeforeLockConcurrencyTest {

    @Autowired
     Seat_holdService seatHoldService;
    @Autowired
    ScheduleSeatRepository scheduleSeatRepository;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    TransactionTemplate transactionTemplate;

    @Autowired
    Seat_holdRepository seatHoldRepository;
    @Test
    void MemberConcurrency100() throws Exception {
        // 1. 회원 생성
        // 2. 이벤트 생성
        // 3. 스케줄 생성
        // 4. 좌석 생성
        // 5. ScheduleSeat 생성
        // 6. 생성된 ID로 동시 선점 실행
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();


        Long[] ids = transactionTemplate.execute(status -> {
            Member member = new Member("임형준");
            memberRepository.createMember(member);

            Event event = new Event("리센느");
            eventRepository.CreateEvent(event);

            Seat seat = new Seat("A1");
            seatRepository.create(seat);

            Schedule schedule = new Schedule(
                    event,
                    LocalDateTime.of(2027, 8, 1, 18, 0)
            );
            scheduleRepository.save(schedule);

            ScheduleSeat scheduleSeat =
                    new ScheduleSeat(seat, schedule, Status.AVAILABLE);
            scheduleSeatRepository.create(scheduleSeat);

            return new Long[]{scheduleSeat.getId(), member.getId()};
        });
        // @Transational를 사용하지않고 이렇게 한이유 ? =>  메서드 블록 단위로 하기떄문에 커밋되지않은 데이터를 볼수없기에
        // 출력하기위해서 이렇게 커밋을 한다음에 id를 받는것임
        //
        Long scheduleSeatId = ids[0];
        Long memberId = ids[1];


        int requestCount = 100;


        ExecutorService es =  Executors.newFixedThreadPool(requestCount);

        CountDownLatch startLatch = new CountDownLatch(1); // 시작 버튼
        CountDownLatch doneLatch = new CountDownLatch(requestCount); // 요청 스레드 개수세는용

        for (int i = 0; i < requestCount; i++) {
            es.submit(() -> {
                try {
                    startLatch.await(); //
                    // 자리 선점 서비스 호출
                    seatHoldService.createHold(scheduleSeatId, memberId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    // 실패 횟수 기록
                    failCount.incrementAndGet();
                    System.out.println(
                            e.getClass().getSimpleName() + ": " + e.getMessage()
                    );
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // 100개 작업 출발
        doneLatch.await();      // 100개 작업 완료까지 main 테스트 스레드 대기

        System.out.println("successCount = " + successCount.get());
        System.out.println("failCount = " + failCount.get());
        // DB 상태와 성공 개수 검증
        es.shutdown();
        List<Seat_hold> holds = seatHoldRepository.findAll();
        System.out.println("seatHoldCount = " + holds.size());
    }
}
