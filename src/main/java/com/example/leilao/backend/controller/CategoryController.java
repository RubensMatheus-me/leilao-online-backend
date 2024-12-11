package com.example.leilao.backend.controller;

import com.example.leilao.backend.model.Category;
import com.example.leilao.backend.model.Person;
import com.example.leilao.backend.service.CategoryService;
import com.example.leilao.backend.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PersonService personService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Category create(@RequestBody Category category, Principal principal) {
        Person person = personService.findByEmail(principal.getName());
        category.setPerson(person);
        return categoryService.create(category);
    }


    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Category update(@RequestBody Category category, Principal principal) {
        Person person = personService.findByEmail(principal.getName());
        Category existingCategory = categoryService.findById(category.getId());

        if (!existingCategory.getPerson().getId().equals(person.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar esta categoria.");
        }

        return categoryService.update(category);
    }

    @GetMapping
    public List<Category> listAll() {
        return categoryService.listAll();
    }

    @GetMapping("/my-categories")
    public List<Category> listMyCategories(Principal principal) {
        Person person = personService.findByEmail(principal.getName());
        return categoryService.getCategoriesByPerson(person);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable("id") Long id, Principal principal) {
        Person person = personService.findByEmail(principal.getName());
        Category category = categoryService.findById(id);

        if (!category.getPerson().getId().equals(person.getId())) {
            throw new RuntimeException("Você não tem permissão para excluir esta categoria.");
        }

        categoryService.delete(id);
    }
}
