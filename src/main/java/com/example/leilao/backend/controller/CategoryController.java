package com.example.leilao.backend.controller;

import com.example.leilao.backend.model.Category;
import com.example.leilao.backend.service.CategoryService;
import com.oracle.svm.core.annotate.Delete;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Category create(@RequestBody Category category) {
        return categoryService.create(category);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Category update(@RequestBody Category category) {
        return categoryService.create(category);
    }

    @GetMapping
    public List<Category> listAll() {
        return categoryService.listAll();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
    }
}
