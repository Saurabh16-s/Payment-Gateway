package com.payflow.gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payflow.gateway.dto.PaymentRequest;
import com.payflow.gateway.dto.PaymentResponse;
import com.payflow.gateway.dto.RefundRequest;
import com.payflow.gateway.service.IdempotencyService;
import com.payflow.gateway.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController{
    private final PaymentService paymentService;
    private final IdempotencyService idempotencyService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody PaymentRequest req,
            @RequestHeader(value="Idempotency-Key",required=false) String idempotencyKey)throws Exception{

        PaymentResponse response=paymentService.createPayment(req);

        if(idempotencyKey!=null){
            idempotencyService.save(idempotencyKey,objectMapper.writeValueAsString(response));
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(
            @PathVariable Long id,
            @Valid @RequestBody RefundRequest req,
            @RequestHeader(value="Idempotency-Key",required=false) String idempotencyKey)throws Exception{

        PaymentResponse response=paymentService.refundPayment(id,req.getAmount());

        if(idempotencyKey!=null){
            idempotencyService.save(idempotencyKey,objectMapper.writeValueAsString(response));
        }

        return ResponseEntity.ok(response);
    }
}