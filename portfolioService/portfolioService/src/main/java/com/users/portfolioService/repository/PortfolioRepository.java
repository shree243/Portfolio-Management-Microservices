package com.users.portfolioService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.users.portfolioService.entity.PortfolioEntry;

public interface PortfolioRepository extends JpaRepository<PortfolioEntry, Long> {
	List<PortfolioEntry> findByEmail(String email);

	Optional<PortfolioEntry> findByEmailAndStockSymbol(String email, String stockSymbol);
}
