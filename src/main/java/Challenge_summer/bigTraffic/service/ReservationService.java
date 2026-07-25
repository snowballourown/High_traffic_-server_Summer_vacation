package Challenge_summer.bigTraffic.service;

import Challenge_summer.bigTraffic.dto.ReservationResponse;
import Challenge_summer.bigTraffic.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public List<ReservationResponse> findMemberReservations(Long memberId) {
        return reservationRepository.findByMemberId(memberId).stream().map(
                reservation ->
                        new ReservationResponse(
                                reservation.getId(),
                                reservation.getPayment().getId(),
                                reservation.getPayment().getSeatHold().getMember().getId()
                                , reservation.getReservedAt())
        ).toList();

    }

}