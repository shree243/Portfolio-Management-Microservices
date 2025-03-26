package com.users.portfolioService.service;

import java.math.BigDecimal;
import java.util.List;

import com.users.portfolioService.dtos.BuyStockRequest;
import com.users.portfolioService.dtos.PortfolioResponse;
import com.users.portfolioService.dtos.PortfolioStockDTO;
import com.users.portfolioService.dtos.PortfolioValuationDTO;
import com.users.portfolioService.dtos.SellStockRequest;

public interface PortfolioService {
	void buyStock(String email, BuyStockRequest request);

	void sellStock(String email, SellStockRequest request);

	List<PortfolioResponse> getPortfolio(String email);

	BigDecimal calculatePortfolioValue(String email);

	List<PortfolioStockDTO> getPortfolioStockDetails(String email);

	String extractEmailFromToken(String token);

	PortfolioValuationDTO calculatePortfolioSummary(String email);
}