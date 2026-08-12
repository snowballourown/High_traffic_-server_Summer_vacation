package challenge_summer.bigtraffic.controller;


import challenge_summer.bigtraffic.dto.scheduleseat.ScheduleSeatRequest;
import challenge_summer.bigtraffic.dto.scheduleseat.ScheduleSeatResponse;
import challenge_summer.bigtraffic.service.ScheduleSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ScheduleSeatController {


    private final ScheduleSeatService scheduleSeatService;


    // List

    @ResponseBody
    @GetMapping("/schedules/{scheduleId}/seats") // 회차 좌석 목록
    public List<ScheduleSeatResponse> findAvailableSeats(
            @PathVariable("scheduleId") Long scheduleId
    ) {
        return scheduleSeatService.findAvailableSeats(scheduleId);
    }



    @PostMapping("/scheduleSeat")
    public ResponseEntity<Void> create(@RequestBody ScheduleSeatRequest scheduleSeatRequest) {
        scheduleSeatService.ScheduleSeatServiceCreate(scheduleSeatRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
