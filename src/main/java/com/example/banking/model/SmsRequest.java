// model/SmsRequest.java
package com.example.banking.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class SmsRequest {
    private String phoneNumber;
    private String message;
    private String customerId;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
