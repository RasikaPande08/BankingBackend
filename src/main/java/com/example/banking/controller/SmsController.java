// controller/SmsController.java
package com.example.banking.controller;


import com.example.banking.model.Account;
import com.example.banking.model.Constants;
import com.example.banking.model.SmsRecord;
import com.example.banking.model.SmsRequest;
import com.example.banking.repository.AccountRepository;
import com.example.banking.repository.SmsRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @Autowired
    private SmsRecordRepository smsRecordRepository;

    @Autowired
    AccountRepository accountRepository;

    @PostMapping("/receiveMessageForBalanceEnquiry")
    public String receiveSmsForBalanceEnquiry(@RequestBody SmsRequest smsRequest) {
        if (Constants.BALANCE_ENQUIRY.toString().equals(smsRequest.getMessage())) {
            Optional<Account> account = accountRepository.findByCustomerid(smsRequest.getCustomerId());
            if (account.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
            }

            String balance = account.get().getBalance();
            System.out.println("Received Message for balance enquiry");
            System.out.println("Balance is : " + balance);
            SmsRecord record = new SmsRecord(
                    smsRequest.getPhoneNumber(),
                    smsRequest.getMessage(),
                    "RECEIVED",
                    LocalDateTime.now()
            );

            smsRecordRepository.save(record);

            return "Balance is : " + balance;

        }
        else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error in message format");
        }
    }


}
