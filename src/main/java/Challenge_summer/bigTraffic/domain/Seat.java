package Challenge_summer.bigTraffic.domain;

import jakarta.persistence.*;


@Entity
public class Seat {

    @Id    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_ID")
    Long Id;

    @Column(nullable = false)
    String SeatName;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getSeatName() {
        return SeatName;
    }

    public void setSeatName(String seatName) {
        SeatName = seatName;
    }
}
