package com.payflow.gateway.service;

import com.payflow.gateway.entity.LedgerEntry;
import com.payflow.gateway.entity.Payment;
import com.payflow.gateway.repository.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LedgerService{
    private final LedgerEntryRepository ledgerRepo;

    @Transactional
    public void recordPayment(Payment payment){
        LedgerEntry debit=new LedgerEntry();
        debit.setAccount(payment.getPayer());
        debit.setAmount(payment.getAmount().negate());
        debit.setType("PAYMENT");
        debit.setPayment(payment);

        LedgerEntry credit=new LedgerEntry();
        credit.setAccount(payment.getPayee());
        credit.setAmount(payment.getAmount());
        credit.setType("PAYMENT");
        credit.setPayment(payment);

        ledgerRepo.save(debit);
        ledgerRepo.save(credit);
    }

    @Transactional
    public void recordRefund(Payment payment,BigDecimal refundAmount){
        LedgerEntry debit=new LedgerEntry();
        debit.setAccount(payment.getPayee());
        debit.setAmount(refundAmount.negate());
        debit.setType("REFUND");
        debit.setPayment(payment);

        LedgerEntry credit=new LedgerEntry();
        credit.setAccount(payment.getPayer());
        credit.setAmount(refundAmount);
        credit.setType("REFUND");
        credit.setPayment(payment);

        ledgerRepo.save(debit);
        ledgerRepo.save(credit);
    }

    public BigDecimal getBalance(Long accountId){
        return ledgerRepo.findByAccountId(accountId).stream()
                .map(LedgerEntry::getAmount)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
}