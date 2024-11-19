package com.example.leilao.backend.service;

import com.example.leilao.backend.dto.MailBodyDTO;
import com.example.leilao.backend.model.Person;
import com.example.leilao.backend.repository.PersonRepository;
import com.example.leilao.backend.request.ChangePasswordRequestDTO;
import com.example.leilao.backend.request.PersonAuthRequestDTO;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.time.Instant;
import java.util.*;

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

    private Integer codeGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }


    public String forgotPassword(PersonAuthRequestDTO personAuthRequestDTO) {
        Optional<Person> person = personRepository.findByEmail(personAuthRequestDTO.getEmail());
        if(person.isPresent()) {
            int code = codeGenerator();
            Person personDatabase = person.get();

            personDatabase.setValidationCode(code);
            personDatabase.setValidationCodeValidity(new Date(System.currentTimeMillis() + 5 * 60 * 1000));
            personRepository.save(personDatabase);

            MailBodyDTO mailBodyDTO = MailBodyDTO.builder()
                    .to(personDatabase.getEmail())
                    .text("This is the OTP for your Forgot Password request: " + code)
                    .subject("OTP for Forgot Password request")
                    .build();

            emailService.sendSimpleEmail(mailBodyDTO);

            return "Código de verificação enviado com sucesso.";
        } else {
            return "Usuário não encontrado.";
        }
    }

    public String validateCode(Integer code, PersonAuthRequestDTO personAuthRequestDTO) {
        Optional<Person> person = personRepository.findByEmailAndValidationCode(personAuthRequestDTO.getEmail(), code);
        if(person.isPresent()) {
            Person personDatabase = person.get();

            if(personDatabase.getValidationCodeValidity().before(Date.from(Instant.now()))) {

                personRepository.clearValidationCode(personDatabase.getEmail());

                return "Código expirado. Por favor, solicite um novo.";
            }
            personRepository.clearValidationCode(personDatabase.getEmail());
            /*
            personRepository.clearValidationCode(personDatabase.getEmail());
            return "Código válido. Prossiga com a alteração de senha.";
            */
        }
        return "Código válido";
    }

    public String changePassword(ChangePasswordRequestDTO changePasswordHandler) {

        if(!Objects.equals(changePasswordHandler.getPassword(), changePasswordHandler.getRepeatPassword())) {
            return "As senhas não coincidem. Tente novamente!";
        }
        Optional<Person> person = personRepository.findByEmail(changePasswordHandler.getEmail());
        if(person.isPresent()) {

            Person personDatabase = person.get();

            personDatabase.setPassword(changePasswordHandler.getPassword());
            personRepository.save(personDatabase);

            return "Senha alterada com sucesso!";
        } else {
            return "Usuário não encontrado.";
        }
    }


    public Person create(Person person) {
        Person personSaved = personRepository.save(person);
        Integer verificationCode = personSaved.getValidationCode();

        try {
            emailService.sendTemplateEmail(
                    personSaved.getEmail(),
                    "Cadastro Efetuado com Sucesso",
                    verificationCode
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return personSaved;
    }

    public Person update(Person person) {
        Person personCreated = personRepository.findById(person.getId()).orElseThrow( () -> new NoSuchElementException("Objeto não encontrado"));
        personCreated.setName(person.getName());
        personCreated.setEmail(person.getEmail());

        return personRepository.save(personCreated);
    }

}
