package Challenge_summer.bigTraffic.service;


import Challenge_summer.bigTraffic.domain.Event;
import Challenge_summer.bigTraffic.dto.EventCreateRequest;
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
    public void EventSave(EventCreateRequest eventCreateRequest) {
        Event event = new Event();
        event.setName(eventCreateRequest.getName());
        eventRepository.CreateEvent(event);
    }


    @Transactional(readOnly = true)
    public Event findById(Long Id) {
        return eventRepository.findByEvent(Id);
    }

    @Transactional(readOnly = true)
    public List<Event> findByAll() {
        return eventRepository.findByAll();
    }

}
