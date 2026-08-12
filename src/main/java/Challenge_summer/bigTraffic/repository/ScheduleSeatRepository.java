package challenge_summer.bigtraffic.repository;


import challenge_summer.bigtraffic.domain.ScheduleSeat;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
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

    public List<ScheduleSeat> findAvailableByScheduleId(Long scheduleId) {
        return em.createNativeQuery(
                        """
                        SELECT *
                        FROM schedule_seat
                        WHERE schedule_id = :scheduleId
                          AND status = 'AVAILABLE'
                        ORDER BY schedule_seat_id
                        """,
                        ScheduleSeat.class
                )
                .setParameter("scheduleId", scheduleId)
                .getResultList();
    }



    public Optional<ScheduleSeat> findByIdWithPessimisticLock(Long id) {
        return Optional.ofNullable(
                em.find(
                        ScheduleSeat.class,
                        id,
                        LockModeType.PESSIMISTIC_WRITE
                )
        );
    }


    public Long count() {
        return em.createQuery("select count(s) from ScheduleSeat s", Long.class).getSingleResult();
    }





}
