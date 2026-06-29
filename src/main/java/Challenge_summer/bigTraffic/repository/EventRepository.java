package Challenge_summer.bigTraffic.repository;


import Challenge_summer.bigTraffic.domain.Event;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class EventRepository {

    private final EntityManager em;

    public void CreateEvent(Event event) {
        em.persist(event);
    }

    public Event findByEvent(Long Id) {
        return em.find(Event.class, Id);
    }

    public List<Event> findByAll() {
        return em.createQuery("select m from Event m", Event.class).getResultList();
    }




}
