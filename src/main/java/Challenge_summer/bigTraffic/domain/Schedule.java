package Challenge_summer.bigTraffic.domain;

import jakarta.persistence.*;

@Entity
public class Schedule {


    @Id @GeneratedValue
    @Column(name = "SCHEDULE_ID")
    Long Id;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    Event event;






}
