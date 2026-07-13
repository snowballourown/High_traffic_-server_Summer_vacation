package Challenge_summer.bigTraffic.controller;


import Challenge_summer.bigTraffic.domain.Event;
import Challenge_summer.bigTraffic.dto.EventCreateRequest;
import Challenge_summer.bigTraffic.service.EventService;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.Value;
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
    public ResponseEntity<Void> SaveEvent(@RequestBody EventCreateRequest eventCreateRequest) {
        eventService.EventSave(eventCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

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
