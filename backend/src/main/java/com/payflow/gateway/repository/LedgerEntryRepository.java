package com.payflow.gateway.repository;


import com.payflow.gateway.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry,Long>{
    List<LedgerEntry> findByAccountId(Long accountId);
}