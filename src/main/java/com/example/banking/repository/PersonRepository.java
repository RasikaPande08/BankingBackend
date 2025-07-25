package com.example.banking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.banking.model.Account;
import com.example.banking.model.Person;

public interface PersonRepository extends JpaRepository<Person, String> {

	Optional<Person> findByCin(String cin);
}
