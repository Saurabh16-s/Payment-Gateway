package com.payflow.gateway.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RefundRequest{
    @NotNull @Positive
    private BigDecimal amount;
}