package Challenge_summer.bigTraffic.repository;

import Challenge_summer.bigTraffic.domain.Reservation;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {

    private final EntityManager em;

    public void create(Reservation reservation) {
        em.persist(reservation);
    }

    public List<Reservation> findByMemberId(Long memberId) {
        return em.createQuery(
                        """
                        select r
                        from Reservation r
                        join r.payment p
                        join p.seatHold h
                        where h.member.id = :memberId
                        """,
                        Reservation.class
                )
                .setParameter("memberId", memberId)
                .getResultList();
    }
}