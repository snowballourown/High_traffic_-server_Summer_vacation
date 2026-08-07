package challenge_summer.bigtraffic.repository;

import challenge_summer.bigtraffic.domain.Reservation;
import challenge_summer.bigtraffic.dto.ReservationResponse;
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

    public List<Reservation> findByMemberIdWithFetchJoin(Long memberId) {
        return em.createQuery("""
                        select r
                        from Reservation r
                        join fetch r.payment p
                        join fetch p.seatHold h
                        join fetch h.member m
                        where m.id = :memberId
                        """, Reservation.class)
                .setParameter("memberId", memberId)
                .getResultList();

    }


    public List<ReservationResponse> findResponseByMemberId(Long memberId) {
        return em.createQuery("""
                select new challenge_summer.bigtraffic.dto.ReservationResponse(
                    r.id,
                    p.id,
                    m.id,
                    r.reservedAt
                )
                from Reservation r
                join r.payment p 
                join p.seatHold h
                join h.member m
                where m.id =:memberId
                """, ReservationResponse.class).setParameter("memberId", memberId).getResultList();
    }






}