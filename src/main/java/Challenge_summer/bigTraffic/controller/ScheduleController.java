package Challenge_summer.bigTraffic.controller;


import Challenge_summer.bigTraffic.dto.schedule.ScheduleRequest;
import Challenge_summer.bigTraffic.dto.schedule.ScheduleResponse;
import Challenge_summer.bigTraffic.service.ScheduleService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService service;

    @PostMapping("/schedules")
    public ResponseEntity<Void> create(@RequestBody ScheduleRequest scheduleRequest) {
        service.createSchedule(scheduleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/schedules")
    @ResponseBody
    public List<ScheduleResponse> findAll() {
        return service.AllSchedule();
    }

    @GetMapping("/events/{eventId}/schedules")
    @ResponseBody
    public List<ScheduleResponse> findEventSchedules(@PathVariable Long eventId) {
        return service.findEventSchedules(eventId);
    }







}

