package com.users.portfolioService.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.users.portfolioService.dtos.BuyStockRequest;
import com.users.portfolioService.dtos.PortfolioResponse;
import com.users.portfolioService.dtos.PortfolioStockDTO;
import com.users.portfolioService.dtos.PortfolioValuationDTO;
import com.users.portfolioService.dtos.SellStockRequest;
import com.users.portfolioService.entity.PortfolioEntry;
import com.users.portfolioService.repository.PortfolioRepository;

@Service
public class PortfolioServiceImpl implements PortfolioService {

	@Autowired
	private PortfolioRepository repository;
	
	@Autowired
	private StockClient stockClient;

	@Override
	public void buyStock(String email, BuyStockRequest request) {
	    PortfolioEntry entry = repository.findByEmailAndStockSymbol(email, request.getStockSymbol())
	            .orElse(new PortfolioEntry());

	    int prevQty = entry.getQuantity() != null ? entry.getQuantity() : 0;
	    BigDecimal prevInvestment = entry.getTotalInvestment() != null ? entry.getTotalInvestment() : BigDecimal.ZERO;

	    int newQty = prevQty + request.getQuantity();
	    BigDecimal newInvestment = request.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
	    BigDecimal updatedInvestment = prevInvestment.add(newInvestment);
	    BigDecimal avgBuyPrice = updatedInvestment.divide(BigDecimal.valueOf(newQty), 2, RoundingMode.HALF_UP);

	    entry.setEmail(email);
	    entry.setStockSymbol(request.getStockSymbol());
	    entry.setQuantity(newQty);
	    entry.setBuyPrice(avgBuyPrice);
	    entry.setTotalInvestment(updatedInvestment);
	    entry.setCreatedAt(entry.getCreatedAt() == null ? LocalDateTime.now() : entry.getCreatedAt());
	    entry.setUpdatedAt(LocalDateTime.now());

	    repository.save(entry);
	}

	@Override
	public void sellStock(String email, SellStockRequest request) {
		PortfolioEntry entry = repository.findByEmailAndStockSymbol(email, request.getStockSymbol())
				.orElseThrow(() -> new RuntimeException("Stock not found"));

		if (entry.getQuantity() < request.getQuantity()) {
			throw new RuntimeException("Not enough stock quantity to sell");
		}

		entry.setSellPrice(request.getPrice());
		entry.setSoldQuantity((entry.getSoldQuantity() == null ? 0 : entry.getSoldQuantity()) + request.getQuantity());
		entry.setQuantity(entry.getQuantity() - request.getQuantity());
		entry.setUpdatedAt(LocalDateTime.now());

		repository.save(entry);
	}

	@Override
	public List<PortfolioResponse> getPortfolio(String email) {
		List<PortfolioEntry> entries = repository.findByEmail(email);
		List<PortfolioResponse> response = new ArrayList<>();

		for (PortfolioEntry entry : entries) {
			PortfolioResponse dto = new PortfolioResponse();
			dto.setStockSymbol(entry.getStockSymbol());
			dto.setQuantity(entry.getQuantity());
			dto.setBuyPrice(entry.getBuyPrice());
			dto.setSellPrice(entry.getSellPrice());
			response.add(dto);
		}
		return response;
	}

//	@Override
//	public BigDecimal calculatePortfolioValue(String email) {
//		List<PortfolioEntry> entries = repository.findByEmail(email);
//		BigDecimal total = BigDecimal.ZERO;
//
//		for (PortfolioEntry entry : entries) {
//			BigDecimal value = entry.getBuyPrice().multiply(BigDecimal.valueOf(entry.getQuantity()));
//			total = total.add(value);
//		}
//
//		return total.setScale(2, RoundingMode.HALF_UP);
//	}
	
	@Override
	public PortfolioValuationDTO calculatePortfolioSummary(String email) {
	    List<PortfolioEntry> entries = repository.findByEmail(email);

	    BigDecimal invested = BigDecimal.ZERO;
	    BigDecimal current = BigDecimal.ZERO;

	    for (PortfolioEntry entry : entries) {
	        BigDecimal livePrice = stockClient.getLivePrice(entry.getStockSymbol());
	        BigDecimal qty = BigDecimal.valueOf(entry.getQuantity());
	        invested = invested.add(entry.getBuyPrice().multiply(qty));
	        current = current.add(livePrice.multiply(qty));
	    }

	    BigDecimal profit = current.subtract(invested);
	    BigDecimal percentageGrowth = invested.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO :
	            profit.divide(invested, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

	    PortfolioValuationDTO dto = new PortfolioValuationDTO();
	    dto.setInvestedValue(invested.setScale(2, RoundingMode.HALF_UP));
	    dto.setCurrentValue(current.setScale(2, RoundingMode.HALF_UP));
	    dto.setProfit(profit.setScale(2, RoundingMode.HALF_UP));
	    dto.setPercentageGrowth(percentageGrowth.setScale(2, RoundingMode.HALF_UP));

	    return dto;
	}
	
	@Override
	public BigDecimal calculatePortfolioValue(String email) {
	    List<PortfolioEntry> entries = repository.findByEmail(email);
	    BigDecimal total = BigDecimal.ZERO;

	    for (PortfolioEntry entry : entries) {
	        BigDecimal livePrice = stockClient.getLivePrice(entry.getStockSymbol());
	        BigDecimal value = livePrice.multiply(BigDecimal.valueOf(entry.getQuantity()));
	        total = total.add(value);
	    }

	    return total.setScale(2, RoundingMode.HALF_UP);
	}
	
	private final String jwtSecret = "my-super-secret-key-for-jwt-signing-my-super-secret-key";

	 @Override
	    public List<PortfolioStockDTO> getPortfolioStockDetails(String email) {
	        List<PortfolioEntry> entries = repository.findByEmail(email);
	        List<PortfolioStockDTO> result = new ArrayList<>();

	        for (PortfolioEntry entry : entries) {
	            BigDecimal currentPrice = stockClient.getLivePrice(entry.getStockSymbol());
	            BigDecimal currentValue = currentPrice.multiply(BigDecimal.valueOf(entry.getQuantity())).setScale(2, RoundingMode.HALF_UP);
	            PortfolioStockDTO dto = new PortfolioStockDTO();
	            dto.setStock(entry.getStockSymbol());
	            dto.setQuantity(entry.getQuantity());
	            dto.setBuyPrice(entry.getBuyPrice());
	            dto.setCurrentPrice(currentPrice);
	            dto.setCurrentValue(currentValue);

	            result.add(dto);
	        }
	        return result;
	    }

    @Override
	public String extractEmailFromToken(String token) {
		Key key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}


}