package Challenge_summer.bigTraffic.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Long id;


    @OneToOne
    @JoinColumn(name = "SEAT_HOLD_ID", unique = true)    //좌석 점유가 같으면 안되니까
    private Seat_hold seatHold;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime paidAt;
    public Payment(Seat_hold seatHold) {
        this.seatHold = seatHold;
        this.paymentStatus = PaymentStatus.PENDING; // 처음시작할때 무조건 pending으로 시작
    }


    public void success() {
        if (paymentStatus != PaymentStatus.PENDING) {
            throw new IllegalArgumentException("이미 처리된 결제입니다.");
        }

        this.paymentStatus = PaymentStatus.SUCCESS;
        this.paidAt = LocalDateTime.now();
    }

    public void fail() {
        if (paymentStatus != PaymentStatus.PENDING) {
            throw new IllegalArgumentException("이미 처리된 결제입니다.");
        }

        this.paymentStatus = PaymentStatus.FAILED;
    }




}
