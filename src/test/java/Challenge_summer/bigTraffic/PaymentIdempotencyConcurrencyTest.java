package challenge_summer.bigtraffic;

import challenge_summer.bigtraffic.domain.*;
import challenge_summer.bigtraffic.dto.payment.PaymentRequest;
import challenge_summer.bigtraffic.dto.payment.PaymentResponse;
import challenge_summer.bigtraffic.repository.*;
import challenge_summer.bigtraffic.service.PaymentService;
import challenge_summer.bigtraffic.service.Seat_holdService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PaymentIdempotencyConcurrencyTest {
    @Autowired
    PaymentRepository paymentRepository;
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
    @Autowired
    PaymentService paymentService;


    @Test
    void 같은_holdId로_동시에_결제하면_중복_요청이_발생한다() throws InterruptedException {

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        ExecutorService es = Executors.newFixedThreadPool(10);
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
                    new ScheduleSeat(seat, schedule, Status.HELD);
            scheduleSeatRepository.create(scheduleSeat);


            Seat_hold seatHold = new Seat_hold(
                    scheduleSeat,
                    member,
                    LocalDateTime.of(2027, 8, 1, 18, 5));

            seatHoldRepository.create(seatHold);



            return new Long[]{seatHold.getId()};
        });

        long seatHoldId = ids[0];

        Set<Long> paymentIds = ConcurrentHashMap.newKeySet();

        int threadCount = 10;

        CountDownLatch cnt = new CountDownLatch(1);
        CountDownLatch executeThread = new CountDownLatch(10);
        for (int i = 0; i < threadCount; i++) {
            es.submit(() -> {

                try {
                    cnt.await();

                    PaymentResponse response
                            = paymentService.createPayment(new PaymentRequest(seatHoldId));
                    paymentIds.add(response.id());


                    successCount.incrementAndGet();


                }  catch (Exception e) {
                    failCount.incrementAndGet();

                    System.out.println(
                            e.getClass().getSimpleName()
                                    + ": "
                                    + e.getMessage()
                    );
                } finally {
                    executeThread.countDown();
                }

            });

        }


        cnt.countDown();//
        executeThread.await();

        System.out.println("success = " + successCount);
        System.out.println("fail = " + failCount);


        assertEquals(10, successCount.get());
        assertEquals(0, failCount.get());
        assertEquals(1, paymentIds.size());



        assertEquals(1L, paymentRepository.count());



    }


}
