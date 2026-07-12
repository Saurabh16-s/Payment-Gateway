package com.payflow.gateway.repository;

import com.payflow.gateway.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long>{
}