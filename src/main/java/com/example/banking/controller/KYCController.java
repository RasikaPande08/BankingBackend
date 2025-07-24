package com.example.banking.controller;

import com.example.banking.model.KYC;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/kyc")
public class KYCController {
    @GetMapping
    public List<KYC> getAll() {
        return Arrays.asList(new KYC());
    }
}
