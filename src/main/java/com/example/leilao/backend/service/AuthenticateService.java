package com.example.leilao.backend.service;

import com.example.leilao.backend.model.Person;
import com.example.leilao.backend.model.PersonAuthDTO;
import com.example.leilao.backend.repository.PersonRepository;
import com.example.leilao.backend.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private JwtService jwtService;

    public String authenticate(PersonAuthDTO authRequest, AuthenticationManager authenticationManager) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(), authRequest.getPassword()));

        Person person = personRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!person.getActive()) {
            return "Conta não verificada. Verifique seu e-mail.";
        }

        return jwtService.generateToken(authentication.getName());
    }
}