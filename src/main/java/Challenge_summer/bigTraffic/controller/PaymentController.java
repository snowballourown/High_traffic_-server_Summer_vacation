package Challenge_summer.bigTraffic.controller;

import Challenge_summer.bigTraffic.dto.ReservationResponse;
import Challenge_summer.bigTraffic.dto.payment.PaymentRequest;
import Challenge_summer.bigTraffic.dto.payment.PaymentResponse;
import Challenge_summer.bigTraffic.service.PaymentService;
import Challenge_summer.bigTraffic.service.ReservationService;
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
