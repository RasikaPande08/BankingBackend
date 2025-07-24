package com.example.banking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.banking.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountNumber(String accountNumber);

}
