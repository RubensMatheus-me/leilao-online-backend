package com.example.leilao.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Entity
@Data
@Getter
@Table(name = "person")
@JsonIgnoreProperties({"authorities"})
public class Person implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message="{name.required}")
    private String name;

    @Column(name = "email")
    @UniqueElements
    private String email;

    @Column(name = "ativo")
    private Boolean active;
    @UniqueElements
    @Column(name = "register_code")
    private String registerCode;
    @Column(name = "register_code_validity")
    private Date registerCodeValidity;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Transient
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public void setPassword(String password) {
        this.password = passwordEncoder.encode(password);
    }

    @Column(name = "validation_code")
    @JsonIgnore
    @UniqueElements
    private Integer validationCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "validation_code_validity")
    private Date validationCodeValidity;

    @OneToMany(mappedBy = "person", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter(value = AccessLevel.NONE)
    @JsonIgnore
    private List<PersonProfile> personProfile;



    public void setPersonProfile(List<PersonProfile> listPersonProfile) {
         for(PersonProfile person:listPersonProfile) {
             person.setPerson(this);
         }
         personProfile = listPersonProfile;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return personProfile.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getProfile().getName()))
                .collect(Collectors.toList());
    }
    @Override
    public String getUsername() {
        return email;
    }
}
