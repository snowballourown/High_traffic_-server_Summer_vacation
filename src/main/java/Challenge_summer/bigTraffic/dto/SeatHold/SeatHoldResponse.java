package Challenge_summer.bigTraffic.dto.SeatHold;

import java.time.LocalDateTime;

public record SeatHoldResponse(
        Long holdId,
        LocalDateTime expiresAt
) {
}