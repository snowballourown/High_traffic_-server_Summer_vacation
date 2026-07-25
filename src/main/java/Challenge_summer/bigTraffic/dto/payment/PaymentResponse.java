package Challenge_summer.bigTraffic.dto.payment;


import Challenge_summer.bigTraffic.domain.PaymentStatus;

public record PaymentResponse(
        Long id,
        Long reservationId,
        PaymentStatus paymentStatus
) { }
