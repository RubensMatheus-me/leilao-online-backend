package com.example.leilao.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "observation")
    private String observation;
    @ManyToOne
    @JoinColumn(name = "person_id")
    Person person;
}
