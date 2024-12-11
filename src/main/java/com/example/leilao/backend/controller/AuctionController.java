package com.example.leilao.backend.controller;

import com.example.leilao.backend.model.Auction;
import com.example.leilao.backend.model.Person;
import com.example.leilao.backend.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @GetMapping("/me")
    public ResponseEntity<Person> getAuthenticatedUser(Authentication authentication) {
        Person authenticatedUser = (Person) authentication.getPrincipal();
        return ResponseEntity.ok(authenticatedUser);
    }

    @PostMapping
    public ResponseEntity<Auction> createAuction(@RequestBody Auction auction) {
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
    public ResponseEntity<Auction> getAuctionById(@PathVariable Long id) {
        Auction auction = auctionService.findById(id);
        return new ResponseEntity<>(auction, HttpStatus.OK);
    }
}
