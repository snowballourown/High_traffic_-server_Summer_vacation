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
    void index_적용x() {

//        /*여기서 굳이 @Transational로 바꿔서 실험한 이유
        //        @Transactional
        //→ 메서드 전체를 하나의 트랜잭션으로 묶을 때
        //→ 단순한 서비스 로직이나 단일 흐름 테스트
        //
        //TransactionTemplate
        //→ 트랜잭션 시작과 종료 위치를 직접 정할 때
        //→ 여러 트랜잭션 분리
        //→ 여러 스레드에 각각 트랜잭션 적용
        //→ 중간 Commit 또는 Rollback 제어
//        */
        createScheduleSeat100();
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

    @Test
    @Transactional
    void 스케줄좌석_만개를_생성한다_index_적용후() {

//        /*여기서 굳이 @Transational로 바꿔서 실험한 이유
        //        @Transactional
        //→ 메서드 전체를 하나의 트랜잭션으로 묶을 때
        //→ 단순한 서비스 로직이나 단일 흐름 테스트
        //
        //TransactionTemplate
        //→ 트랜잭션 시작과 종료 위치를 직접 정할 때
        //→ 여러 트랜잭션 분리
        //→ 여러 스레드에 각각 트랜잭션 적용
        //→ 중간 Commit 또는 Rollback 제어
//        */
        createScheduleSeat100();
        em.flush();

        em.createNativeQuery(
                """
                CREATE INDEX idx_schedule_seat_status
                ON schedule_seat(status)
                """
        ).executeUpdate(); //index이름를 설정하고 on뒤에 앤티티이름 (인덱스로 엔트리뷰트)




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
    @Test
    @Transactional
    void 스케줄좌석_만개를_생성한다_index_적용후_AVAILABLE() {

//        /*여기서 굳이 @Transational로 바꿔서 실험한 이유
        //        @Transactional
        //→ 메서드 전체를 하나의 트랜잭션으로 묶을 때
        //→ 단순한 서비스 로직이나 단일 흐름 테스트
        //
        //TransactionTemplate
        //→ 트랜잭션 시작과 종료 위치를 직접 정할 때
        //→ 여러 트랜잭션 분리
        //→ 여러 스레드에 각각 트랜잭션 적용
        //→ 중간 Commit 또는 Rollback 제어
//        */
        createScheduleSeat100();
        em.flush();

        em.createNativeQuery(
                """
                CREATE INDEX idx_schedule_seat_status
                ON schedule_seat(status)
                """
        ).executeUpdate(); //index이름를 설정하고 on뒤에 앤티티이름 (인덱스로 엔트리뷰트)



        List<?> result = em.createNativeQuery(
                """
                EXPLAIN ANALYZE
                SELECT *
                FROM schedule_seat
                WHERE status = 'AVAILABLE'
                """
        ).getResultList();

        result.forEach(System.out::println);
    }
   @Test
    @Transactional
    void 스케줄좌석_만개를_생성한다_index_적용후_AVAILABLE_optimizer무시() {

//        /*여기서 굳이 @Transational로 바꿔서 실험한 이유
        //        @Transactional
        //→ 메서드 전체를 하나의 트랜잭션으로 묶을 때
        //→ 단순한 서비스 로직이나 단일 흐름 테스트
        //
        //TransactionTemplate
        //→ 트랜잭션 시작과 종료 위치를 직접 정할 때
        //→ 여러 트랜잭션 분리
        //→ 여러 스레드에 각각 트랜잭션 적용
        //→ 중간 Commit 또는 Rollback 제어
//        */
        createScheduleSeat100();
        em.flush();

        em.createNativeQuery(
                """
                CREATE INDEX idx_schedule_seat_status
                ON schedule_seat(status)
                """
        ).executeUpdate(); //index이름를 설정하고 on뒤에 앤티티이름 (인덱스로 엔트리뷰트)



        List<?> result = em.createNativeQuery(
              """
              EXPLAIN ANALYZE
              SELECT *
              FROM schedule_seat
              IGNORE INDEX (idx_schedule_seat_status)
              WHERE status = 'AVAILABLE';
                """
        ).getResultList();

        result.forEach(System.out::println);
    }


    @Test
    @Transactional
    void 복합인덱스_적용전_scheduleId와status로조회한다() {
        createScheduleSeat100();
        em.flush();


        Number result = (Number) em.createNativeQuery( // Number은 Integer이나 Long 이런 클래스의 부모클래스
                "SELECT MIN(schedule_id) FROM schedule_seat"
        ).getSingleResult();

        //DB에서 숫자를 가져오는데 DB/JPA환경에따라 Long,Integer같은구체적 타입이 달라질수도있어서
        //이렇게 가져옴

        long scheduleId = result.longValue();


        List<?> explain = em.createNativeQuery(
                """
                EXPLAIN ANALYZE
                SELECT *
                FROM schedule_seat
                WHERE schedule_id = :scheduleId
                    AND status = 'AVAILABLE'                               
                """)
                .setParameter("scheduleId", scheduleId)
                .getResultList();


        explain.forEach(System.out::println);
    }


    @Test
    @Transactional
    void 복합인덱스_적용후_scheduleId와status로조회한다() {
        createScheduleSeat100();
        em.flush();

        em.createNativeQuery(
                """
                CREATE INDEX idx_schedule_seat_schedule_status
                ON schedule_seat(schedule_id, status)
                """
        ).executeUpdate();

        Number result = (Number) em.createNativeQuery( // Number은 Integer이나 Long 이런 클래스의 부모클래스
                "SELECT MIN(schedule_id) FROM schedule_seat"
        ).getSingleResult();


        //DB에서 숫자를 가져오는데 DB/JPA환경에따라 Long,Integer같은구체적 타입이 달라질수도있어서
        //이렇게 가져옴

        long scheduleId = result.longValue();


        List<?> explain = em.createNativeQuery(
                """
                EXPLAIN ANALYZE
                SELECT *
                FROM schedule_seat
                WHERE schedule_id = :scheduleId
                    AND status = 'AVAILABLE'                               
                """)
                .setParameter("scheduleId", scheduleId)
                .getResultList();


        explain.forEach(System.out::println);
    }


    @Test
    @Transactional
    void 복합인덱스_효율_테스트_Only스케줄id() {
        createScheduleSeat100();
        em.flush();
        //실험하다가 왜 3개를 동시에 진행을 안하는지 궁금할것임



        em.createNativeQuery(
                """
                CREATE INDEX idx_schedule_seat_schedule_status
                ON schedule_seat(schedule_id, status)
                """
        ).executeUpdate();

        Number result = (Number) em.createNativeQuery( // Number은 Integer이나 Long 이런 클래스의 부모클래스
                "SELECT MIN(schedule_id) FROM schedule_seat"
        ).getSingleResult();


        //DB에서 숫자를 가져오는데 DB/JPA환경에따라 Long,Integer같은구체적 타입이 달라질수도있어서
        //이렇게 가져옴

        long scheduleId = result.longValue();


        List<?> explain = em.createNativeQuery(
                        """
                        EXPLAIN ANALYZE
                        SELECT *
                        FROM schedule_seat
                        FORCE INDEX (idx_schedule_seat_schedule_status)
                        WHERE status = 'AVAILABLE'
                        """
                )
                .getResultList();

        explain.forEach(System.out::println);
    }









    private void createScheduleSeat100() {
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
    }


}
