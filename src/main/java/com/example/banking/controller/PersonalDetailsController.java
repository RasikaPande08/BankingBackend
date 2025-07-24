package com.example.banking.controller;

import com.example.banking.model.PersonalDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/personaldetails")
public class PersonalDetailsController {
    @GetMapping
    public List<PersonalDetails> getAll() {
        return Arrays.asList(new PersonalDetails());
    }
}
