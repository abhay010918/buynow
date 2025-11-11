package com.buynow.controller;

import com.buynow.dto.createPaymentRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PaymentController {

    public ResponseEntity<Map<String, String>> createPaymentIntent(
            @RequestBody createPaymentRequest req
    ) throws StripeException {
        Map<String, Object> prams = new HashMap<>();
        prams.put("amount", req.getAmount());
        prams.put("currency", req.getCurrency());
        prams.put("orderId", req.getOrderId());

        PaymentIntent paymentIntent = PaymentIntent.create(prams);
        return ResponseEntity.ok(Map.of("clientSecret", paymentIntent.getClientSecret()));

    }
}
