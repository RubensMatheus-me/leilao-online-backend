package com.example.leilao.backend.repository;

import com.example.leilao.backend.model.Category;
import com.example.leilao.backend.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByPerson(Person person);
}
