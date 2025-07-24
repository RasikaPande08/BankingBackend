package com.example.banking.model;

import org.springframework.stereotype.Component;

@Component
public class UserVerificationRequest {
	
    private String customerId;
    private String password;
    public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

    // Getters and setters
}
