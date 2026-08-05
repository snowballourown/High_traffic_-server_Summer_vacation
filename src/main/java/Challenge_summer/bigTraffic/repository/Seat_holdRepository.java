package challenge_summer.bigtraffic.repository;


import challenge_summer.bigtraffic.domain.Seat_hold;
import challenge_summer.bigtraffic.domain.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class Seat_holdRepository {


    private final EntityManager em;


    public void create(Seat_hold seatHold) {
        em.persist(seatHold);
    }


    public List<Seat_hold> findExpired(LocalDateTime now) {
        return em.createQuery(
                        "select h from Seat_hold h " +
                                "where h.expiresAt <= :now " +
                                "and h.scheduleSeat.status = :status",
                        Seat_hold.class
                )
                .setParameter("now", now)
                .setParameter("status", Status.HELD)
                .getResultList();
    }

    public void delete(Seat_hold seatHold) {
        em.remove(seatHold);
    }


    public List<Seat_hold> findAll() {
        return em.createQuery("select h from Seat_hold h", Seat_hold.class).getResultList();
    }


    public Optional<Seat_hold> findById(Long id) {
        return Optional.ofNullable(em.find(Seat_hold.class, id));
    }


    public Optional<Seat_hold> findByIdWithPessimisticLock(Long id) {
        return Optional.ofNullable(
                em.find(
                        Seat_hold.class,
                        id,
                        LockModeType.PESSIMISTIC_WRITE
                )
        );
    }




}
