package Challenge_summer.bigTraffic.controller;


import Challenge_summer.bigTraffic.domain.Event;
import Challenge_summer.bigTraffic.dto.event.EventRequest;
import Challenge_summer.bigTraffic.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class EventController {

    private final EventService eventService;


    @PostMapping("/event")
    public ResponseEntity<Void> SaveEvent(@RequestBody EventRequest eventRequest) {
        eventService.EventSave(eventRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


//  ################  ## 이것도 고칠것
    @GetMapping("/event")
    @ResponseBody
    public List<Event> findAll() {
        return eventService.findByAll();
    }

    @GetMapping("/event/{eventId}")
    @ResponseBody
    public Event findByEventId(@PathVariable Long eventId) {
        return eventService.findById(eventId);
    }




}
