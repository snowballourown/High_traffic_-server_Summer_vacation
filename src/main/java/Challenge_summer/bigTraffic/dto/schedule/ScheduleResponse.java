package Challenge_summer.bigTraffic.dto.schedule;


import Challenge_summer.bigTraffic.domain.Event;
import Challenge_summer.bigTraffic.domain.Schedule;
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
