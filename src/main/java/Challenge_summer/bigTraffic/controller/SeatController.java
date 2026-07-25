package Challenge_summer.bigTraffic.controller;


import Challenge_summer.bigTraffic.domain.Member;
import Challenge_summer.bigTraffic.dto.seat.SeatCreateRequest;
import Challenge_summer.bigTraffic.dto.seat.SeatResponse;
import Challenge_summer.bigTraffic.service.SeatService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SeatController {


    final private SeatService seatService;


    @GetMapping("/seats")
    @ResponseBody
    public List<SeatResponse> getSeats() {
        return seatService.seatResponseList();
    }

    @PostMapping("/seats")
    public ResponseEntity<Void> createSeat(@RequestBody SeatCreateRequest seatCreateRequest) {
        seatService.SeatCreate(seatCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
