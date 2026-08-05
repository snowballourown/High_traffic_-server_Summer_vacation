package challenge_summer.bigtraffic.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.logging.Level;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Seat_hold {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOLD_ID")

    private Long Id;


    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @JoinColumn(name = "SCHEDULE_SEAT_ID")
    private ScheduleSeat scheduleSeat;


    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @JoinColumn(name = "MEMBER_ID")
    private Member member;



    private LocalDateTime expiresAt;


    public Seat_hold(ScheduleSeat scheduleSeat, Member member, LocalDateTime expiresAt) {
        this.scheduleSeat = scheduleSeat;
        this.member = member;
        this.expiresAt = expiresAt;
    }

}
