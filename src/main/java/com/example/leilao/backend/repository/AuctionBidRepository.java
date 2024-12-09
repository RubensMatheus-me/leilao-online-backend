package com.example.leilao.backend.repository;

import com.example.leilao.backend.model.AuctionBid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionBidRepository extends JpaRepository<AuctionBid, Long> {
}
