package com.payflow.gateway.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
public class LedgerEntry{
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    private BigDecimal amount; // positive=credit, negative=debit

    private String type; // PAYMENT, REFUND

    @ManyToOne
    private Payment payment;

    private Instant createdAt=Instant.now();
}