package challenge_summer.bigtraffic.domain;

import challenge_summer.bigtraffic.dto.schedule.ScheduleRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    private LocalDateTime startTime;



    public Schedule(Event event, LocalDateTime startTime) {
        this.event = event;
        this.startTime = startTime;
    }



}