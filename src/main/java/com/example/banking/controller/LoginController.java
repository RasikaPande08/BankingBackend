package com.example.banking.controller;

import com.example.banking.dto.LoginRequest;
import com.example.banking.model.Account;
import com.example.banking.repository.AccountRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // Replace with your frontend URL
@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private AccountRepository accountRepository;

    // Login API - expects only customerid and password
    @PostMapping
    public String login(@RequestBody LoginRequest request, HttpSession session) {
        Optional<Account> account = accountRepository.findByCustomerid(request.getCustomerid());

        if (account.isPresent() && account.get().getPassword().equals(request.getPassword())) {
            session.setAttribute("customerid", request.getCustomerid());
            return "✅ Login successful";
        } else {
            return "❌ Invalid customer ID or password";
        }
    }

    // Fetch balance using session after login
    @GetMapping("/balance")
    public String getBalanceAfterLogin(HttpSession session) {
        String customerid = (String) session.getAttribute("customerid");
        if (customerid == null) {
            return "❌ Please login first.";
        }

        Optional<Account> account = accountRepository.findByCustomerid(customerid);
        return account.map(acc -> "Your balance is ₹" + acc.getBalance())
                .orElse("❌ Account not found");
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "✅ Logged out successfully";
    }
}
