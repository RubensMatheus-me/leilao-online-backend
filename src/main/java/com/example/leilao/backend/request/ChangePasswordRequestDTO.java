package com.example.leilao.backend.request;

import lombok.Data;

@Data
public class ChangePasswordRequestDTO {
    private String email;
    private String password;
    private String repeatPassword;

}
