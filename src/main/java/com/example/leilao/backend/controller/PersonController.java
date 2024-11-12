package com.example.leilao.backend.controller;

import com.example.leilao.backend.model.Person;
import com.example.leilao.backend.model.PersonAuthDTO;
import com.example.leilao.backend.repository.PersonRepository;
import com.example.leilao.backend.request.PersonAuthRequestDTO;
import com.example.leilao.backend.response.PersonAuthResponseDTO;
import com.example.leilao.backend.security.JwtService;
import com.example.leilao.backend.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
public class PersonController {
    @Autowired
    private PersonService personService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;



    @PostMapping("/login")
    public PersonAuthResponseDTO authenticateUser(@RequestBody PersonAuthDTO authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(), authRequest.getPassword()));
        return new PersonAuthResponseDTO(authRequest.getEmail(), jwtService.generateToken(authentication.getName()));
    }

    @PostMapping("/forgot-password-request")
    public String passwordCodeRequest(@RequestBody PersonAuthRequestDTO person) {
        return personService.forgotPassword(person);
    }
    @PostMapping
    public Person create(@Valid @RequestBody Person person) {
        return personService.create(person);
    }

    @PutMapping
    public Person update(@Valid @RequestBody Person person) {
        return personService.create(person);
    }
}
