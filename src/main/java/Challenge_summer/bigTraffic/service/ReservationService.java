package challenge_summer.bigtraffic.service;

import challenge_summer.bigtraffic.dto.ReservationResponse;
import challenge_summer.bigtraffic.repository.ReservationRepository;
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
        return reservationRepository.findResponseByMemberId(memberId);
    }

}