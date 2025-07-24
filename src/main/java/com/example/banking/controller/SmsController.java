// controller/SmsController.java
package com.example.banking.controller;


import com.example.banking.model.Constants;
import com.example.banking.model.SmsRecord;
import com.example.banking.model.SmsRequest;
import com.example.banking.repository.SmsRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @Autowired
    private SmsRecordRepository smsRecordRepository;

    @PostMapping("/receive")
    public String receiveSms(@RequestBody SmsRequest smsRequest) {
        if (Constants.BALANCE_ENQUIRY.toString().equals(smsRequest.getMessage())) {
            Long balance = 0L;
            System.out.println("Received Message for balance enquiry");
            System.out.println("Balance is : " + balance);
            SmsRecord record = new SmsRecord(
                    smsRequest.getPhoneNumber(),
                    smsRequest.getMessage(),
                    "RECEIVED",
                    LocalDateTime.now()
            );

            smsRecordRepository.save(record);

        }
        return "message received";
    }

    @PostMapping("/send")
    public String sendSms(@RequestBody SmsRequest smsRequest) {
        System.out.println("Sms Sent to : " + smsRequest.getPhoneNumber() + "with Message : "+smsRequest.getMessage());

        System.out.println("Send Message for balance enquiry");
        SmsRecord record = new SmsRecord(
                smsRequest.getPhoneNumber(),
                smsRequest.getMessage(),
                "RECEIVED",
                LocalDateTime.now()
        );

        smsRecordRepository.save(record);
        return "SMS Received successfully!";
    }
}
