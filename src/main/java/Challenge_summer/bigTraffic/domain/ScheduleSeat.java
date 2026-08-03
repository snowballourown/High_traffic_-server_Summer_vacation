package challenge_summer.bigtraffic.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_SEAT_ID")
    private Long id;
    private String name;


    @ManyToOne
    @JoinColumn(name = "SCHEDULE_ID")
    private Schedule schedule;


    @ManyToOne
    @JoinColumn(name = "SEAT_ID")
    private Seat seat;


    @Enumerated(EnumType.STRING)
    @Setter
    private Status status;
//
//    @Version
//    private Long Version;


    public ScheduleSeat(Seat seat, Schedule schedule, Status status) {
        this.schedule = schedule;
        this.seat = seat;
        this.status = status;
    }

    public void hold() {
        if (status != Status.AVAILABLE) {
            throw new IllegalArgumentException("선점할 수 없는 좌석입니다.");
        }

        this.status = Status.HELD;
    }

    public void release() {
        if (status == Status.HELD) {
            this.status = Status.AVAILABLE;
        }
    }

    public void confirm() {
        if (status == Status.HELD) {
            this.status = Status.CONFIRMED;
        }
    }



}
