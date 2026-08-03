package challenge_summer.bigtraffic.dto.schedule;


import challenge_summer.bigtraffic.domain.Event;
import challenge_summer.bigtraffic.domain.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;


public record ScheduleResponse (
        Long ScheduleId,
        Long eventId,
        String eventName,
        LocalDateTime localDateTime


){
   public static ScheduleResponse from(Schedule schedule) {
      return new ScheduleResponse(schedule.getId(),
              schedule.getEvent().getId(),
              schedule.getEvent().getName(),
              schedule.getStartTime());
   }

}
