package Challenge_summer.bigTraffic.repository;


import Challenge_summer.bigTraffic.domain.Event;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class EventRepository {

    private final EntityManager em;

    public void CreateEvent(Event event) {
        em.persist(event);
    }

    public Optional<Event> findById(Long Id) {
        return Optional.ofNullable(em.find(Event.class, Id));
    }



    public Optional<Event> findByName(String name) {
        List<Event> result = em.createQuery(
                        "select e from Event e where e.name = :name",
                        Event.class
                )
                .setParameter("name", name)
                .getResultList();

        return result.stream().findAny(); // 조건에 맞는거 아무거나 하나 꺼내는것

    }

    public List<Event> findAll() {
        return em.createQuery("select m from Event m", Event.class).getResultList();
    }




}
