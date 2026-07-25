package Challenge_summer.bigTraffic.dto;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long reservationId,
        Long paymentId,
        Long memberId,
        LocalDateTime reservedAt
) {
}