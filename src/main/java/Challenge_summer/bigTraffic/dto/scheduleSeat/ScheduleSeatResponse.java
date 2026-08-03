package challenge_summer.bigtraffic.dto.scheduleseat;

import challenge_summer.bigtraffic.domain.Status;

public record ScheduleSeatResponse(
        Long scheduleSeatId,
        Long scheduleId,
        Long seatId,
        Status status
){}
