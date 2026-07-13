package Challenge_summer.bigTraffic.domain;

import jakarta.persistence.*;


@Entity
public class Seat {

    @Id    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_ID")
    Long Id;




}
