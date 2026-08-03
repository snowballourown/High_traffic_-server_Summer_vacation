package challenge_summer.bigtraffic.controller;

import challenge_summer.bigtraffic.dto.ReservationResponse;
import challenge_summer.bigtraffic.dto.payment.PaymentRequest;
import challenge_summer.bigtraffic.dto.payment.PaymentResponse;
import challenge_summer.bigtraffic.service.PaymentService;
import challenge_summer.bigtraffic.service.ReservationService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestBody PaymentRequest request
    ) {
        PaymentResponse response = paymentService.createPayment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


}
