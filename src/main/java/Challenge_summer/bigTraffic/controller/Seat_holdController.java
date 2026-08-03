package challenge_summer.bigtraffic.controller;


import challenge_summer.bigtraffic.dto.seathold.SeatHoldRequest;
import challenge_summer.bigtraffic.dto.seathold.SeatHoldResponse;
import challenge_summer.bigtraffic.service.Seat_holdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class Seat_holdController {


    private final Seat_holdService seatHoldService;


    @PostMapping("/schedule-seats/{scheduleSeatId}/holds")
    public ResponseEntity<SeatHoldResponse> createHold(
            @PathVariable Long scheduleSeatId,  //
            @RequestBody SeatHoldRequest request // 맴버ID는 이쪽
    ) {

        SeatHoldResponse response = seatHoldService.createHold(
                scheduleSeatId,
                request.memberId()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/schedule-seats/holds")
    public List<SeatHoldResponse> allHold() {
        return seatHoldService.seatHoldResponseAll();
    }



}
