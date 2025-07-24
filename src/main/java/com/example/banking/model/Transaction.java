package com.example.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
/*
 * public class Transaction {
 *
 * @Id
 *
 * @GeneratedValue(strategy = GenerationType.IDENTITY) private Long cin; private
 * String date; private String transactionRef; // it will give description of
 * what transation has been done private String transactionType; private String
 * transactionAmount; private String balance; }
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID")
    private Long transactionId;

    @Column(name = "ACCOUNTNUMBER", nullable = false)
    private String accountNumber;

    @Column(name = "TRANSACTION_TYPE", nullable = false)
    private String transactionType; // e.g., Credit, Debit

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "TRANSACTION_DATE", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    private String status; // e.g., Success, Pending

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getters only (if immutability is desired via Lombok)
    public Long getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}
