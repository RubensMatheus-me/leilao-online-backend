package com.example.leilao.backend.repository;

import com.example.leilao.backend.model.Auction;
import com.example.leilao.backend.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByPerson(Person person);

    Auction findByIdAndPerson(Long id, Person person);

    @Query("SELECT a FROM Auction a LEFT JOIN FETCH a.category WHERE a.id = :id")
    Optional<Auction> findByIdWithCategory(Long id);
}

