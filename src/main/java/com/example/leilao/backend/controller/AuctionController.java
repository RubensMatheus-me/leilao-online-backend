package com.example.leilao.backend.controller;

import com.example.leilao.backend.model.Auction;
import com.example.leilao.backend.model.Person;
import com.example.leilao.backend.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auction")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @GetMapping("/me")
    public ResponseEntity<Person> getAuthenticatedUser(Authentication authentication) {
        Person authenticatedUser = (Person) authentication.getPrincipal();
        return ResponseEntity.ok(authenticatedUser);
    }

    @PostMapping
    public ResponseEntity<Auction> createAuction(@RequestBody Auction auction, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        log.info("Usuário autenticado: {}", authentication.getName());

        if (auction.getImageUrl() == null || auction.getImageUrl().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Auction createdAuction = auctionService.create(auction);
        return new ResponseEntity<>(createdAuction, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Auction> updateAuction(@PathVariable Long id, @RequestBody Auction auction) {
        auction.setId(id);
        Auction updatedAuction = auctionService.update(auction);
        return new ResponseEntity<>(updatedAuction, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuction(@PathVariable Long id) {
        auctionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/my-auctions")
    public ResponseEntity<List<Auction>> listUserAuctions() {
        List<Auction> auctions = auctionService.listAll();
        return new ResponseEntity<>(auctions, HttpStatus.OK);
    }

    @GetMapping("/public")
    public ResponseEntity<List<Auction>> listPublicAuctions() {
        List<Auction> auctions = auctionService.listAllPublic();
        return new ResponseEntity<>(auctions, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public HttpEntity<Optional<Auction>> getAuctionById(@PathVariable Long id) {
        Optional<Auction> auction = auctionService.findById(id);
        return new ResponseEntity<>(auction, HttpStatus.OK);
    }
}
