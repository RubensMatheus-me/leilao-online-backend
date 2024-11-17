package com.example.leilao.backend.service;

import com.example.leilao.backend.model.Person;
import com.example.leilao.backend.repository.PersonRepository;
import com.example.leilao.backend.request.PersonAuthRequestDTO;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PersonService implements UserDetailsService {//inserir, alterar, delete?
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return personRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    public String forgotPassword(PersonAuthRequestDTO personAuthRequestDTO) {
        Optional<Person> person = personRepository.findByEmail(personAuthRequestDTO.getEmail());
        if(person != null) {
            Person personDatabase = person.get();
            //gerar numero random
            personDatabase.setValidationCode(12345);
            personDatabase.setValidationCodeValidity(new Date());
            personRepository.save(personDatabase);

        }
        return "";
    }

    public Person create(Person person) {
        Person personSaved = personRepository.save(person);

        Context context = new Context();
        context.setVariable("name", personSaved.getName());
        try {
            emailService.sendTemplateEmail(
                    personSaved.getEmail(),
                    "Cadastro Efetuado com Sucesso", context,
                    "emailWelcome");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return personSaved;
    }
    public Person update(Person person) {
        Person personCreated = personRepository.findById(person.getId()).orElseThrow( () -> new NoSuchElementException("Objeto não encontrado"));
        personCreated.setName(person.getName());
        personCreated.setName(person.getEmail());

        return personRepository.save(personCreated);
    }

}
