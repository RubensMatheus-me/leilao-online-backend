package com.example.leilao.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.property.access.spi.SetterMethodImpl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
    @JsonIgnore
    private String password;
    @Column(name = "validation_code")
    @JsonIgnore
    private String validationCode;
    @Column(name = "validation_code_validity")
    @JsonIgnore
    private LocalDateTime validationCodeValidity;

    @OneToMany(mappedBy = "person", orphanRemoval = true, cascade = CascadeType.ALL)
    @Setter(value = AccessLevel.NONE)
    private List<PersonProfile> personProfile;

    /*
    @Temporal(TemporalType.TIMESTAMP)
    private Date validationCodeValidity;
     */

    public void setPersonProfile(List<PersonProfile> listPersonProfile) {
         for(PersonProfile person:listPersonProfile) {
             person.setPerson(this);
         }
         personProfile = listPersonProfile;
    }
}
