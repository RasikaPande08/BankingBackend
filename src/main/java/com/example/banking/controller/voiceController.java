package com.example.banking.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.banking.model.Account;
import com.example.banking.model.Transaction;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.TransactionRepository;

@RestController
@RequestMapping("/api/voice")
public class voiceController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;


    @GetMapping("/{customerid}/{password}/bal")
    public ResponseEntity<?> getBal(@PathVariable String customerid,
                                    @PathVariable String password) {

        Optional<Account> account = accountRepository.findByCustomeridAndPassword(customerid, password);
        if (account.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("❌ Invalid credentials: customer ID or password is incorrect.");
        }


        return ResponseEntity.ok(account.get().getBalance());
    }

    @GetMapping("/ivr")
    public void startIVR() throws Exception {
        FinancialVoiceIVR financialVoiceIVR = new FinancialVoiceIVR();
        financialVoiceIVR.start();
    }

    @GetMapping("/{customerid}/{password}/lastfive")
    public ResponseEntity<?> getLastFiveAmounts(@PathVariable String customerid,
                                                @PathVariable String password) {

        Optional<Account> account = accountRepository.findByCustomeridAndPassword(customerid, password);

        if (account.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("❌ Invalid credentials: customer ID or password is incorrect.");
        }

        List<BigDecimal> amounts = transactionRepository
                .findByAccountNumber(account.get().getAccountNumber())
                .stream()
                .map(Transaction::getAmount)
                .toList();

        return ResponseEntity.ok(amounts);

    }

    @PostMapping("/transfer/{fromAcc}/{toAcc}/{amount}")
    public ResponseEntity<?> transferFunds(@PathVariable String fromAcc,
                                           @PathVariable String toAcc,
                                           @PathVariable BigDecimal amount) {
        Optional<Account> senderOpt = accountRepository.findByAccountNumber(fromAcc);

        if (senderOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ sender accounts not found.");
        }

        Account sender = senderOpt.get();

        BigDecimal senderBalance = new BigDecimal(sender.getBalance());
        if (senderBalance.compareTo(amount) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("🚫 Insufficient balance in sender account.");
        }

        // 🧮 Update balances
        sender.setBalance(senderBalance.subtract(amount).toString());

        accountRepository.save(sender);

        // 💾 Log transaction (simplified, can create two entries if needed)
        Transaction txn = new Transaction();
        txn.setAccountNumber(fromAcc);
        txn.setTransactionType("Transfer");
        txn.setAmount(amount);
        txn.setTransactionDate(LocalDateTime.now());
        txn.setDescription("Transfer to " + toAcc);
        txn.setStatus("Success");

        transactionRepository.save(txn);

        return ResponseEntity.ok("✅ ₹" + amount + " transferred from " + fromAcc + " to " + toAcc);
    }


}
