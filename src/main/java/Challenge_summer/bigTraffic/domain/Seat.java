package Challenge_summer.bigTraffic.domain;

import jakarta.persistence.*;


@Entity
public class Seat {

    @Id    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_ID")
    Long id;

    @Column(nullable = false)
    String seatName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatName() {
        return seatName;
    }

    public void setSeatName(String seatName) {
        this.seatName = seatName;
    }
}
