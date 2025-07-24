package com.example.banking.repository;

import com.example.banking.model.SmsRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRecordRepository extends JpaRepository<SmsRecord, Long> {
}
