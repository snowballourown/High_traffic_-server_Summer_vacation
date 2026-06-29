package Challenge_summer.bigTraffic.domain;


import jakarta.persistence.*;

@Entity
public class ScheduleSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_SEAT_ID")
    private Long Id;
    private String name;


    @ManyToOne
    @JoinColumn(name = "SCHEDULE_ID")
    private Schedule schedule;


    @ManyToOne
    @JoinColumn(name = "SEAT_ID")
    private Seat seat;

    @Enumerated(EnumType.STRING)
    private Status status;

}
