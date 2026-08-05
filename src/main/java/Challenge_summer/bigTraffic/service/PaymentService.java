package challenge_summer.bigtraffic.service;


import challenge_summer.bigtraffic.domain.Payment;
import challenge_summer.bigtraffic.domain.Reservation;
import challenge_summer.bigtraffic.domain.Seat_hold;
import challenge_summer.bigtraffic.domain.Status;
import challenge_summer.bigtraffic.dto.ReservationResponse;
import challenge_summer.bigtraffic.dto.payment.PaymentRequest;
import challenge_summer.bigtraffic.dto.payment.PaymentResponse;
import challenge_summer.bigtraffic.repository.PaymentRepository;
import challenge_summer.bigtraffic.repository.ReservationRepository;
import challenge_summer.bigtraffic.repository.Seat_holdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final Seat_holdRepository seatHoldRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {

        Seat_hold seatHold =
                seatHoldRepository.findByIdWithPessimisticLock(paymentRequest.holdId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "해당 선점 정보가 없습니다."
                                ));

        Optional<Payment> existingPayment =
                paymentRepository.findBySeatHoldId(seatHold.getId());

        if (existingPayment.isPresent()) {
            Payment payment = existingPayment.get();

            return new PaymentResponse(
                    payment.getId(),
                    payment.getPaymentStatus()
            );
        }



        // 케이스 1:
// 선점 만료 시간이 이미 지났지만,
// 1초 주기의 스케줄러가 아직 해당 SeatHold를 삭제하지 못한 순간에
// 결제 요청이 들어올 수 있다.
//
// expiresAt이 현재 시간과 같아도 결제 기한이 끝난 것으로 처리한다.
        if (!seatHold.getExpiresAt().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "선점 시간이 만료되었습니다."
            );
        }


// 케이스 2:
// 이전 결제 요청이 이미 성공해서
// ScheduleSeat 상태가 CONFIRMED로 변경된 경우다.
//
// 같은 holdId로 다시 결제를 요청했거나,
// 이미 예약이 확정된 좌석에 결제를 시도한 상황이다.
        if (seatHold.getScheduleSeat().getStatus() == Status.CONFIRMED) {
            throw new IllegalArgumentException(
                    "이미 결제가 완료된 좌석입니다."
            );
        }


        // 케이스 3:
        // 만료 시간은 지나지 않았고 CONFIRMED도 아니지만,
        // 좌석 상태가 HELD가 아닌 경우다.
        //
        // 현재 Status가 AVAILABLE, HELD, CONFIRMED 세 개뿐이므로
        // 위에서 CONFIRMED를 처리한 뒤 이 조건에 걸리면 AVAILABLE이다.
        //
        // 선점이 해제됐거나, 결제 실패 등으로 좌석이 다시
        // 예약 가능한 상태가 된 경우이므로 결제를 진행하면 안 된다.

        if (seatHold.getScheduleSeat().getStatus() != Status.HELD) {
            throw new IllegalArgumentException(
                    "현재 유효한 선점 상태가 아닙니다."
            );
        }




        // 존재안할시 값저장
        Payment payment = new Payment(seatHold);

        payment.success(); // 결제성공상태변경
        seatHold.getScheduleSeat().confirm(); // 선점 상태변경

        paymentRepository.create(payment);
        Reservation reservation = new Reservation(payment);
        reservationRepository.create(reservation);



        return new PaymentResponse(
                payment.getId(),
                payment.getPaymentStatus()
        );
    }






}
