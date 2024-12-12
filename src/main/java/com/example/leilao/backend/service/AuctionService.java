package com.example.leilao.backend.service;

import com.example.leilao.backend.model.Auction;
import com.example.leilao.backend.model.Person;
import com.example.leilao.backend.repository.AuctionRepository;
import com.example.leilao.backend.security.AuthPersonProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AuthPersonProvider authPersonProvider;

    public Auction create(Auction auction) {
        auction.setPerson(authPersonProvider.getAuthenticatedUser());

        return auctionRepository.save(auction);
    }


    public Auction update(Auction auction) {
        Auction auctionSaved = auctionRepository.findById(auction.getId()).orElseThrow(() -> new NoSuchElementException("Leilão não encontrado"));

        auctionSaved.setTitle(auction.getTitle());
        auctionSaved.setDescription(auction.getDescription());
        auctionSaved.setStartDateTime(auction.getStartDateTime());
        auctionSaved.setEndDateTime(auction.getEndDateTime());
        auctionSaved.setStatus(auction.getStatus());
        auctionSaved.setObservation(auction.getObservation());
        auctionSaved.setIncrementValue(auction.getIncrementValue());

        return auctionRepository.save(auctionSaved);
    }

    public void delete(Long id) {
        Auction auctionSaved = auctionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Leilão não encontrado"));

        auctionRepository.delete(auctionSaved);
    }

    public List<Auction> listAll() {
        Person authenticatedUser = authPersonProvider.getAuthenticatedUser();

        return auctionRepository.findByPerson(authenticatedUser);
    }

    public List<Auction> listAllPublic() {
        return auctionRepository.findAll();
    }

    public Optional<Auction> findById(Long id) {
        Optional<Auction> auction = auctionRepository.findById(id);
        return auction;
    }
}
