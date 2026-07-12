package com.payflow.gateway.repository;

import com.payflow.gateway.entity.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey,String>{
}