package com.example.leilao.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "validation_code")
    private String validationCode;
    @Column(name = "validation_code_validity")
    private LocalDateTime validationCodeValidity;

    /*
    @Temporal(TemporalType.TIMESTAMP)
    private Date validationCodeValidity;
     */
}
