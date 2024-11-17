package com.example.leilao.backend.controller;

import com.example.leilao.backend.model.Person;
import com.example.leilao.backend.model.PersonAuthDTO;
import com.example.leilao.backend.repository.PersonRepository;
import com.example.leilao.backend.request.PersonAuthRequestDTO;
import com.example.leilao.backend.response.PersonAuthResponseDTO;
import com.example.leilao.backend.security.JwtService;
import com.example.leilao.backend.service.PersonService;
import com.example.leilao.backend.utils.ChangePasswordHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<PersonAuthResponseDTO> authenticateUser(@RequestBody @Valid PersonAuthDTO authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(), authRequest.getPassword()));

        String token = jwtService.generateToken(authentication.getName());
        return ResponseEntity.ok(new PersonAuthResponseDTO(authRequest.getEmail(), token));
    }

    @PostMapping("/forgot-password-request")
    public ResponseEntity<String> passwordCodeRequest(@RequestBody @Valid PersonAuthRequestDTO person) {
        String response = personService.forgotPassword(person);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password-validate")
    public ResponseEntity<String> authenticateCode(
            @RequestParam Integer code,
            @RequestBody @Valid PersonAuthRequestDTO email) {
        String response = personService.validateCode(code, email);
        return response.equals("Código expirado") || response.equals("Código ou e-mail inválido.")
                ? ResponseEntity.badRequest().body(response)
                : ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePasswordHandler(
            @RequestBody @Valid ChangePasswordHandler changePassword,
            @RequestBody @Valid PersonAuthRequestDTO email) {
        String response = personService.changePassword(changePassword, email);
        return response.equals("Usuário não encontrado.") || response.equals("As senhas não coincidem. Tente novamente!")
                ? ResponseEntity.badRequest().body(response)
                : ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) {
        Person createdPerson = personService.create(person);
        return ResponseEntity.ok(createdPerson);
    }

    @PutMapping
    public ResponseEntity<Person> update(@Valid @RequestBody Person person) {
        Person updatedPerson = personService.update(person);
        return ResponseEntity.ok(updatedPerson);
    }
}