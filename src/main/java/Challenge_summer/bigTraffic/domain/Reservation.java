package challenge_summer.bigtraffic.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAYMENT_ID", unique = true)
    private Payment payment;

    private LocalDateTime reservedAt;

    public Reservation(Payment payment) {
        this.payment = payment;
        this.reservedAt = LocalDateTime.now();
    }
}