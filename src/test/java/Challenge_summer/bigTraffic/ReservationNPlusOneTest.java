package challenge_summer.bigtraffic;


import challenge_summer.bigtraffic.domain.*;
import challenge_summer.bigtraffic.dto.ReservationResponse;
import challenge_summer.bigtraffic.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReservationNPlusOneTest {

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


    private Long create10MemberReservation() {
        return transactionTemplate.execute(status -> {
            Member member = new Member("테스트 회원");
            memberRepository.createMember(member);

            Event event = new Event("N+1 테스트 이벤트");
            eventRepository.CreateEvent(event);
            for (int i = 1; i <= 10; i++) { // 한사람의 예약 10개만들기

                Seat seat = new Seat("A"+i);
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

                Payment payment = new Payment(seatHold);
                payment.success();
                paymentRepository.create(payment);

                Reservation reservation = new Reservation(payment);
                reservationRepository.create(reservation);

            }





                return member.getId();
        });

    }



    @Test
    void 회원의_예약_10개를_조회하면_N플러스1_쿼리가_발생한다() {
        Long memberId = create10MemberReservation();

        transactionTemplate.execute(status -> {
            List<Reservation> reservations =
                    reservationRepository.findByMemberId(memberId);

            for (Reservation reservation : reservations) {
                Payment payment = reservation.getPayment();

                // Payment 프록시 초기화
                System.out.println(payment.getPaymentStatus());

                // SeatHold 프록시 초기화
                System.out.println(payment.getSeatHold().getExpiresAt());

                // Member 프록시 초기화
                System.out.println(
                        payment.getSeatHold().getMember().getName()
                );
            }

            return null;
        });
    }

    @Test
    void 페치조인으로_예약과_연관엔티티를_한번에_조회한다() {
        Long memberId = create10MemberReservation();

        transactionTemplate.execute(status -> {
            List<Reservation> reservations =
                    reservationRepository.findByMemberIdWithFetchJoin(memberId);

            for (Reservation reservation : reservations) {
                Payment payment = reservation.getPayment();

                // Payment 프록시 초기화
                System.out.println(payment.getPaymentStatus());

                // SeatHold 프록시 초기화
                System.out.println(payment.getSeatHold().getExpiresAt());

                // Member 프록시 초기화
                System.out.println(
                        payment.getSeatHold().getMember().getName()
                );
            }
            assertEquals(10, reservations.size());
            return null;
        });

    }



    @Test
    void DTO_직접_조회로_필요한_컬럼만_한번에_조회한다() {
        Long memberId = create10MemberReservation();

        List<ReservationResponse> responses =
                transactionTemplate.execute(status ->
                        reservationRepository.findResponseByMemberId(memberId)
                );

        assertEquals(10, responses.size());

        for (ReservationResponse response : responses) {
            System.out.println(response);
        }
    }

}
