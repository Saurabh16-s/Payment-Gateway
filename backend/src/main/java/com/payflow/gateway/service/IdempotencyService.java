package com.payflow.gateway.service;

import com.payflow.gateway.entity.IdempotencyKey;
import com.payflow.gateway.repository.IdempotencyKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdempotencyService{
    private final IdempotencyKeyRepository repo;

    public Optional<String> getCachedResponse(String key){
        return repo.findById(key).map(IdempotencyKey::getResponseBody);
    }

    public void save(String key,String responseBody){
        IdempotencyKey entry=new IdempotencyKey();
        entry.setKey(key);
        entry.setResponseBody(responseBody);
        repo.save(entry);
    }
}