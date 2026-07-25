package Challenge_summer.bigTraffic.controller;


import Challenge_summer.bigTraffic.dto.scheduleSeat.ScheduleSeatRequest;
import Challenge_summer.bigTraffic.dto.scheduleSeat.ScheduleSeatResponse;
import Challenge_summer.bigTraffic.service.ScheduleSeatService;
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
public class ScheduleSeatController {


    private final ScheduleSeatService scheduleSeatService;


    // List

    @ResponseBody
    @GetMapping("/scheduleSeat")
    public List<ScheduleSeatResponse> findAll() {
        return scheduleSeatService.findAll();
    }


    @PostMapping("/scheduleSeat")
    public ResponseEntity<Void> create(@RequestBody ScheduleSeatRequest scheduleSeatRequest) {
        scheduleSeatService.ScheduleSeatServiceCreate(scheduleSeatRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
