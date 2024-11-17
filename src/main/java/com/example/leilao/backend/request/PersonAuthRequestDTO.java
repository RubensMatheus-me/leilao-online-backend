package com.example.leilao.backend.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class PersonAuthRequestDTO {
    @Email(message = "E-mail inválido.")
    private String email;
    private String password;
}
