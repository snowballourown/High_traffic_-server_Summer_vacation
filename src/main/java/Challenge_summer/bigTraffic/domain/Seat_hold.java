package Challenge_summer.bigTraffic.domain;


import jakarta.persistence.*;

@Entity
public class Seat_hold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOLD_ID")
    private Long Id;
    private String name;


    @ManyToOne
    @JoinColumn(name = "SCHEDULE_SEAT_ID")
    private ScheduleSeat scheduleSeat;



    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;







}
