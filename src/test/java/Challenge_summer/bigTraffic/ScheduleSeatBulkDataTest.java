package challenge_summer.bigtraffic;

import challenge_summer.bigtraffic.domain.*;
import challenge_summer.bigtraffic.repository.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ScheduleSeatBulkDataTest {

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
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    EntityManager em;

    @Test
    @Transactional
    void 스케줄좌석_만개를_생성한다() {
        Event event = new Event("최초 리센느 팬콘서트");
        eventRepository.CreateEvent(event);

        List<Seat> seats = new ArrayList<>();

        // 좌석 100개를 먼저 생성
        for (int i = 1; i <= 100; i++) {
            Seat seat = new Seat("A" + i);
            seatRepository.create(seat);
            seats.add(seat);
        }

        long startTime = System.currentTimeMillis();

        // 회차 100개 생성
        for (int scheduleNumber = 1; scheduleNumber <= 100; scheduleNumber++) {
            Schedule schedule = new Schedule(
                    event,
                    LocalDateTime.now().plusDays(scheduleNumber)
            );
            scheduleRepository.save(schedule);

            // 각 회차에 좌석 100개 배치
            for (int seatNumber = 1; seatNumber <= 100; seatNumber++) {
                int sequence = (scheduleNumber - 1) * 100 + seatNumber;

                Status status =
                        sequence % 20 == 0 ? Status.HELD
                                : sequence % 10 == 0 ? Status.CONFIRMED
                                : Status.AVAILABLE;

                scheduleSeatRepository.create(
                        new ScheduleSeat(
                                seats.get(seatNumber - 1),
                                schedule,
                                status
                        )
                );
            }
        }

        long count = scheduleSeatRepository.count();
        long elapsed = System.currentTimeMillis() - startTime;

        assertEquals(10_000L, count);
        System.out.println("ScheduleSeat 개수 = " + count);
        System.out.println("생성 시간 = " + elapsed + "ms");


        em.flush();


        List<?> result = em.createNativeQuery(
                """
                EXPLAIN ANALYZE
                SELECT *
                FROM schedule_seat
                WHERE status = 'HELD'
                """
        ).getResultList();

        result.forEach(System.out::println);

    }





}
