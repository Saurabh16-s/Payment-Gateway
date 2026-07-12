package com.payflow.gateway.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Account{
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
}