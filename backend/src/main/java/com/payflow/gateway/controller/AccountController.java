package com.payflow.gateway.controller;


import com.payflow.gateway.entity.Account;
import com.payflow.gateway.repository.AccountRepository;
import com.payflow.gateway.service.LedgerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController{
    private final AccountRepository accountRepo;
    private final LedgerService ledgerService;

    @PostMapping
    public Account createAccount(@RequestBody Map<String,String> body){
        Account account=new Account();
        account.setName(body.get("name"));
        return accountRepo.save(account);
    }

    @GetMapping("/{id}/balance")
    public Map<String,Object> getBalance(@PathVariable Long id){
        if(!accountRepo.existsById(id))throw new EntityNotFoundException("Account not found");
        BigDecimal balance=ledgerService.getBalance(id);
        return Map.of("accountId",id,"balance",balance);
    }
}