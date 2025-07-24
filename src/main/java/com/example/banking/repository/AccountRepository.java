package com.example.banking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.banking.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByCustomerid(String customerid);

    Optional<Account> findByCustomeridAndPassword(String customerid, String password);

    Optional<Account> findByAccountNumber(String accountNumber);
}
