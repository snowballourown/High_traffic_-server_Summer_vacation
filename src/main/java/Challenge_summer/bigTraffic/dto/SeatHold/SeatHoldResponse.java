package challenge_summer.bigtraffic.dto.seathold;

import java.time.LocalDateTime;

public record SeatHoldResponse(
        Long holdId,
        LocalDateTime expiresAt
) {
}