package com.example.banking.controller;

import com.example.banking.model.Person;
import com.example.banking.repository.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/personaldetails")
public class PersonalDetailsController {
	
	@Autowired
	PersonRepository personRepository;
    
	@GetMapping("/person/{cin}")
    public ResponseEntity<?> fetchPerson(@PathVariable String cin) {
		Optional<Person> account = personRepository.findByCin(cin);
		if (account.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("❌ Invalid credentials: cin.");
        }

        return ResponseEntity.ok(account.get());
    }
}
