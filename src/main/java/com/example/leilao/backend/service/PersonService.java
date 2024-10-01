package com.example.leilao.backend.service;

import com.example.leilao.backend.model.Person;
import com.example.leilao.backend.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class PersonService {//inserir, alterar, delete?
    @Autowired
    private PersonRepository personRepository;

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
