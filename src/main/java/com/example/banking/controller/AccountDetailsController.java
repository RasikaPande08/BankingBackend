package com.example.banking.controller;

import com.example.banking.model.Account;
import com.example.banking.model.UserVerificationRequest;
import com.example.banking.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accountdetails")
public class AccountDetailsController {
    @Autowired
    AccountRepository accountRepository;

    @GetMapping
    public List<Account> getAll() {
        return Arrays.asList(new Account());
    }

    @GetMapping("/{customerid}")
    public Account getBal(@PathVariable String customerid) {
        Optional<Account> account = accountRepository.findByCustomerid(customerid);
        if (account.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }
        return account.get(); // Returns the entire Account object, including balance
    }

    @GetMapping("/verify/{customerid}/{password}")
    public ResponseEntity<?> getVerifyUser(@PathVariable String customerid,
                                           @PathVariable String password) {
        System.out.println("hiiiiiiii,{} {}" + customerid + "....." + password);
        Optional<Account> account = accountRepository.findByCustomeridAndPassword(customerid, password);

        if (account.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("❌ Invalid credentials: customer ID or password is incorrect.");
        }

        return ResponseEntity.ok("Valid User");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody UserVerificationRequest request) {
        Optional<Account> accountOpt = accountRepository.findByCustomeridAndPassword(request.getCustomerId(), request.getPassword());
        System.out.println("hiiiiiiii,{} {}" + request.getCustomerId() + "....." + request.getPassword());
        if (accountOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Customer not found.");
        }

        Account account = accountOpt.get();
        if (!account.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("🚫 Invalid credentials.");
        }

        return ResponseEntity.ok("✅ Verification successful for " + request.getCustomerId());
    }

    @GetMapping("/verify/{customerId}")
	public ResponseEntity<String> verifyUser(@PathVariable String customerId) {
		Optional<Account> accountOpt = accountRepository.findByCustomerid(customerId);
		System.out.println("hiiiiiiii,{} {}"+customerId);
		if (accountOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Customer not found.");
		}

		return ResponseEntity.ok("Verification successful for " + customerId);
	}
}
