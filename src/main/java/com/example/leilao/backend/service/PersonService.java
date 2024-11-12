package com.example.leilao.backend.service;

import com.example.leilao.backend.model.Person;
import com.example.leilao.backend.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class PersonService implements UserDetailsService {//inserir, alterar, delete?
    @Autowired
    private PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return personRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Person create(Person person) {
        return personRepository.save(person);
    }
    public Person update(Person person) {
        Person personCreated = personRepository.findById(person.getId()).orElseThrow( () -> new NoSuchElementException("Objeto não encontrado"));
        personCreated.setName(person.getName());
        personCreated.setName(person.getEmail());

        return personRepository.save(personCreated);
    }

}
