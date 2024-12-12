package com.example.leilao.backend.service;

import com.example.leilao.backend.model.Category;
import com.example.leilao.backend.model.Person;
import com.example.leilao.backend.repository.AuctionRepository;
import com.example.leilao.backend.repository.CategoryRepository;
import com.example.leilao.backend.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PersonRepository personRepository;


    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada."));
    }

    @Transactional
    public Category create(Category category) {
        return categoryRepository.save(category);

    }

    public Category update(Category category) {
        Category categorySave = categoryRepository.findById(category.getId()).orElseThrow( () -> new NoSuchElementException("Objeto não encontrado"));
        categorySave.setName(category.getName());
        return categoryRepository.save(categorySave);
    }

    public void delete(Long id) {
        Category categorySaved = categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Objeto não encontrado"));
        categoryRepository.delete(categorySaved);
    }

    public List<Category> listAll() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByPerson(Person person) {
        return categoryRepository.findByPerson(person);
    }

}
