package com.example.banking.dto;

public class LoginRequest {
    private String customerid;
    private String password;

    // Getters and setters
    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
