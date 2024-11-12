package com.example.leilao.backend.response;

import com.example.leilao.backend.request.PersonAuthRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonAuthResponseDTO {
    private String email;
    private String token;
}
