package Challenge_summer.bigTraffic.repository;


import Challenge_summer.bigTraffic.domain.Payment;
import jakarta.persistence.EntityManager;
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
}
