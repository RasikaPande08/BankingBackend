package com.example.banking.controller;

import com.example.banking.model.Investment;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/investment")
public class InvestmentController {
    @GetMapping
    public List<Investment> getAll() {
        return Arrays.asList(new Investment());
    }
}
