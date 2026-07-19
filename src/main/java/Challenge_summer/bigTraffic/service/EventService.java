package Challenge_summer.bigTraffic.service;


import Challenge_summer.bigTraffic.domain.Event;
import Challenge_summer.bigTraffic.dto.event.EventRequest;
import Challenge_summer.bigTraffic.dto.event.EventResponse;
import Challenge_summer.bigTraffic.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional
    public void EventSave(EventRequest eventCreateRequest) {
        Event event = new Event();
        event.setName(eventCreateRequest.getName());
        eventRepository.CreateEvent(event);
    }

    @Transactional(readOnly = true)
    public EventResponse findByName(String name) {
        Event event = eventRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("공연이 존재하지 않습니다."));

        EventResponse eventResponse = new EventResponse(event.getName());

        return eventResponse;
    }


    @Transactional(readOnly = true)
    public Event findById(Long Id) {
        return eventRepository.findById(Id);
    }

    @Transactional(readOnly = true)
    public List<Event> findByAll() {
        return eventRepository.findAll();
    }

}
