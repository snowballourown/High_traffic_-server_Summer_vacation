package Challenge_summer.bigTraffic.domain;


import jakarta.persistence.*;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private Long Id;
    private String name;



    @OneToOne
    @JoinColumn(name = "SEAT_HOLD_ID")
    private Seat_hold seat_hold;
}
