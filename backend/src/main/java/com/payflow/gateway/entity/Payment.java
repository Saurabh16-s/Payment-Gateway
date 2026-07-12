package com.payflow.gateway.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
public class Payment{
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @ManyToOne
    private Account payer;

    @ManyToOne
    private Account payee;

    private Instant createdAt=Instant.now();
}