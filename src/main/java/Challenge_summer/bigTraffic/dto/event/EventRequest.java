package challenge_summer.bigtraffic.dto.event;


import java.time.LocalDateTime;

public record EventRequest(
        Long eventId,
        String name
) {


}
