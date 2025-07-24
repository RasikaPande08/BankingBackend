package com.example.banking.controller;

import com.example.banking.model.Account;
import com.example.banking.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    
    public String getBal(String customerid) {
		
		Optional<Account> account = accountRepository.findByCustomerid(customerid);
		if (account.isEmpty()) {
	        return "❌ Invalid credentials: customer ID or password is incorrect.";
	    }

		return account.get().getBalance();
    }
}
