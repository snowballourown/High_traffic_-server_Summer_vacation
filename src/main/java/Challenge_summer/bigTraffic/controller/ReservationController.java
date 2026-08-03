package challenge_summer.bigtraffic.controller;


import challenge_summer.bigtraffic.dto.ReservationResponse;
import challenge_summer.bigtraffic.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;


    @GetMapping("/members/{memberId}/reservations")
    public List<ReservationResponse> findMemberReservations(
            @PathVariable Long memberId
    ) {
        return reservationService.findMemberReservations(memberId);
    }
}
