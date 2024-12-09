package com.example.leilao.backend.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "auction_bid")
@Data
public class AuctionBid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter(AccessLevel.NONE)
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "auction_bid")
    private Auction auction;
    @ManyToOne
    @JoinColumn(name = "person_bid")
    private Person person;

    @PrePersist
    public void prePersist() {
        this.dateTime = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.dateTime = LocalDateTime.now();
    }
}
