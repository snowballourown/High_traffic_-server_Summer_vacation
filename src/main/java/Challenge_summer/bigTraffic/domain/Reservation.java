package Challenge_summer.bigTraffic.domain;


import jakarta.persistence.*;

@Entity
public class Reservation {


    @Id @GeneratedValue
    @Column(name = "RESERVATION_ID")
    Long Id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;


    @OneToOne
    @JoinColumn(name = "PAYMENT_ID")
    private Payment payment;


    @OneToOne
    @JoinColumn(name = "SCHEDULE_SEAT_ID")
    private ScheduleSeat scheduleSeat;

}
