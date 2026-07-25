package Challenge_summer.bigTraffic.repository;


import Challenge_summer.bigTraffic.domain.ScheduleSeat;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ScheduleSeatRepository {

    private final EntityManager em;


    public void create(ScheduleSeat scheduleSeat) {
        em.persist(scheduleSeat);
    }

    public Optional<ScheduleSeat> findById(Long id) {
        return Optional.ofNullable(em.find(ScheduleSeat.class, id));
    }


    public List<ScheduleSeat> findAll() {
        return em.createQuery("select s from ScheduleSeat s", ScheduleSeat.class)
                .getResultList();
    }






}
