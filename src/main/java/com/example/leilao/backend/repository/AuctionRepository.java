package com.example.leilao.backend.repository;

import com.example.leilao.backend.model.Auction;
import com.example.leilao.backend.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByPerson(Person person);

    Auction findByIdAndPerson(Long id, Person person);
}
