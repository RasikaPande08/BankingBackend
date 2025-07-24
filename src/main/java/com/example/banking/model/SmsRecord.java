package com.example.banking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
public class SmsRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientNumber;
    private String message;
    private String status;
    private LocalDateTime timestamp;

    public SmsRecord(String recipientNumber, String message, String status, LocalDateTime timestamp) {
        this.recipientNumber = recipientNumber;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters & Setters
    // ... (generate using IDE or Lombok if preferred)
}