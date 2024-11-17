package com.example.leilao.backend.request;

import lombok.Data;

@Data
public class PersonAuthRequestDTO {
    private String email;
    private String password;
}
