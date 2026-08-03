package challenge_summer.bigtraffic.dto;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long reservationId,
        Long paymentId,
        Long memberId,
        LocalDateTime reservedAt
) {
}