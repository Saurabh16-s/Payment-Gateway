package com.payflow.gateway.dto;

import com.payflow.gateway.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentResponse{
    private Long id;
    private BigDecimal amount;
    private PaymentStatus status;
    private Long payerId;
    private Long payeeId;
}