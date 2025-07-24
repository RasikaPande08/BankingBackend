package com.example.banking.controller;


import com.example.banking.model.Transaction;
import com.example.banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;




@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
	
	@Autowired
	TransactionRepository transactionRepository ; 
	
//    @GetMapping
//    public List<Transaction> getAll() {
//        return Arrays.asList(new Transaction());
//    }
//    
//     //Get all transactions
//    @GetMapping
//    public List<Transaction> getAllTransactions() {
//        return transactionRepository.findAll();
//    }
//
//    // Get transaction by ID
//    @GetMapping("/{id}/{accountNumber}")
//    public Transaction getTransactionById(@PathVariable Long id) {
//        return transactionRepository.findById(id).orElse(null);
//    }
}
