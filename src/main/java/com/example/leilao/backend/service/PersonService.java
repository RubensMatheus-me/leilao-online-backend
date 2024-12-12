package com.example.leilao.backend.service;

import com.example.leilao.backend.dto.MailBodyDTO;
import com.example.leilao.backend.model.Person;
import com.example.leilao.backend.model.PersonAuthDTO;
import com.example.leilao.backend.repository.PersonRepository;
import com.example.leilao.backend.request.ChangePasswordRequestDTO;
import com.example.leilao.backend.request.PersonAuthRequestDTO;
import com.example.leilao.backend.security.JwtService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
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

    @Transactional
    public Person getPersonWithAuthorities(Long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new RuntimeException("Person not found"));
        return person;
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
                    .text(String.valueOf(code))
                    .subject("OTP for Forgot Password request")
                    .build();

            try {
                emailService.sendTemplateEmail(mailBodyDTO);
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Erro ao enviar o e-mail de verificação.";
            }

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

    @Transactional
    public Person create(Person person) {
        person.setActive(false);
        person.setRegisterCode(UUID.randomUUID().toString());
        person.setRegisterCodeValidity(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); //24horas

        Person personSaved = personRepository.save(person);

        try {
            String activationLink = "http://localhost:8080/api/person/activate?token=" + personSaved.getRegisterCode();

            MailBodyDTO mailBodyDTO = MailBodyDTO.builder()
                    .to(personSaved.getEmail())
                    .subject("Confirmação de Cadastro")
                    .text(activationLink)
                    .build();

            emailService.sendTemplateVerifiedEmail(mailBodyDTO);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao enviar o e-mail de ativação.");
        }

        return personSaved;
    }

    public String activateUser(String token) {
        Optional<Person> personOptional = personRepository.findByRegisterCode(token);
        if (personOptional.isEmpty()) {
            return "Token inválido ou expirado.";
        }
        Person person = personOptional.get();

        if (person.getRegisterCodeValidity().before(Date.from(Instant.now()))) {
            return "Token expirado. Solicite um novo registro.";
        }

        person.setActive(true);
        person.setRegisterCode(null);
        person.setRegisterCodeValidity(null);
        personRepository.save(person);

        return "Usuário ativado com sucesso!";
    }


    public Person update(Person person) {
        Person personCreated = personRepository.findById(person.getId()).orElseThrow( () -> new NoSuchElementException("Objeto não encontrado"));
        personCreated.setName(person.getName());
        personCreated.setEmail(person.getEmail());

        return personRepository.save(personCreated);
    }

    public Person findByEmail(String email) {
        return personRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

}
