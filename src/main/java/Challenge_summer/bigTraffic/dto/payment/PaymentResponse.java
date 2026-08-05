package challenge_summer.bigtraffic.dto.payment;


import challenge_summer.bigtraffic.domain.PaymentStatus;

public record PaymentResponse(
        Long id,
        PaymentStatus paymentStatus
) { }
