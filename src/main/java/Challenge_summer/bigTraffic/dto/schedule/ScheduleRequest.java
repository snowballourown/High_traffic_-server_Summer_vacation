package Challenge_summer.bigTraffic.dto.schedule;


import lombok.Getter;

import java.time.LocalDateTime;



public record ScheduleRequest (

        Long eventId,
        LocalDateTime localDateTime
){

}
