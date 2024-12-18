package com.example.leilao.backend.repository;

import com.example.leilao.backend.model.Person;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @EntityGraph(attributePaths = "personProfile")
    Optional<Person> findByEmail(String email);

    Optional<Person> findByEmailAndValidationCode(String email, Integer validationCode);

    Optional<Person> findByRegisterCode(String registerCode);

    @Modifying
    @Transactional
    @Query("UPDATE Person p SET p.password = ?2 WHERE p.email = ?1")
    void updatePassword(String email, String password);

    @Modifying
    @Transactional
    @Query("UPDATE Person p SET p.validationCode = NULL, p.validationCodeValidity = NULL WHERE p.email = ?1")
    void clearValidationCode(String email);
}
