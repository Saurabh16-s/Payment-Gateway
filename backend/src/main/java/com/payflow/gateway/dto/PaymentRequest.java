package com.payflow.gateway.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequest{
    @NotNull
    private Long payerId;

    @NotNull
    private Long payeeId;

    @NotNull @Positive
    private BigDecimal amount;
}
