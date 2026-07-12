package com.payflow.gateway.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Entity
@Data
public class IdempotencyKey{
    @Id
    private String key;

    @Column(columnDefinition="TEXT")
    private String responseBody;

    private Instant createdAt=Instant.now();
}