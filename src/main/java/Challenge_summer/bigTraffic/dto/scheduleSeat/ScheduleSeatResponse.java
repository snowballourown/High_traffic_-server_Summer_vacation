package Challenge_summer.bigTraffic.dto.scheduleSeat;

import Challenge_summer.bigTraffic.domain.Status;

public record ScheduleSeatResponse(
        Long scheduleSeatId,
        Long scheduleId,
        Long seatId,
        Status status
){}
