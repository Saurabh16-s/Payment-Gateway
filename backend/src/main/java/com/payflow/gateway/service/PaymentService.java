package com.payflow.gateway.service;

import com.payflow.gateway.dto.PaymentRequest;
import com.payflow.gateway.dto.PaymentResponse;
import com.payflow.gateway.entity.*;
import com.payflow.gateway.exception.InsufficientBalanceException;
import com.payflow.gateway.repository.AccountRepository;
import com.payflow.gateway.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService{
    private final AccountRepository accountRepo;
    private final PaymentRepository paymentRepo;
    private final LedgerService ledgerService;

    @Transactional
    public PaymentResponse createPayment(PaymentRequest req){
        Account payer=accountRepo.findById(req.getPayerId())
                .orElseThrow(()->new EntityNotFoundException("Payer not found"));
        Account payee=accountRepo.findById(req.getPayeeId())
                .orElseThrow(()->new EntityNotFoundException("Payee not found"));

        BigDecimal payerBalance=ledgerService.getBalance(payer.getId());
        if(payerBalance.compareTo(req.getAmount())<0){
            throw new InsufficientBalanceException("Payer has insufficient balance");
        }

        Payment payment=new Payment();
        payment.setAmount(req.getAmount());
        payment.setPayer(payer);
        payment.setPayee(payee);
        payment.setStatus(PaymentStatus.CAPTURED);
        payment=paymentRepo.save(payment);

        ledgerService.recordPayment(payment);

        return toResponse(payment);
    }

    @Transactional
    public PaymentResponse refundPayment(Long paymentId,BigDecimal refundAmount){
        Payment payment=paymentRepo.findById(paymentId)
                .orElseThrow(()->new EntityNotFoundException("Payment not found"));

        if(refundAmount.compareTo(payment.getAmount())>0){
            throw new InsufficientBalanceException("Refund amount exceeds original payment");
        }

        ledgerService.recordRefund(payment,refundAmount);
        payment.setStatus(PaymentStatus.REFUNDED);
        payment=paymentRepo.save(payment);

        return toResponse(payment);
    }

    private PaymentResponse toResponse(Payment payment){
        return new PaymentResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPayer().getId(),
                payment.getPayee().getId()
        );
    }
}