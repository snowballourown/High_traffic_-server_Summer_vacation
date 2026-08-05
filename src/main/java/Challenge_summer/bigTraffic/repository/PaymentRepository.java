package challenge_summer.bigtraffic.repository;


import challenge_summer.bigtraffic.domain.Payment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepository {


    private final EntityManager em;

    public void create(Payment payment) {
        em.persist(payment);
    }

    public Optional<Payment> findById(Long id) {
        return Optional.ofNullable(
                em.find(Payment.class, id)
        );
    }

    public Optional<Payment> findByIdWithPessimisticLock(Long id) {

        return Optional.ofNullable(
                em.find(Payment.class
                        , id
                , LockModeType.PESSIMISTIC_WRITE)
        );
    }



    public Optional<Payment> findBySeatHoldId(Long holdId) {
        return em.createQuery(
                        """
                        select p
                        from Payment p
                        where p.seatHold.Id = :holdId
                        """,
                        Payment.class
                )
                .setParameter("holdId", holdId)
                .getResultStream()
                .findFirst();
    }


    public Long count() {
        return em.createQuery("select count(p) from Payment p"
                , Long.class).getSingleResult();
    }

}
