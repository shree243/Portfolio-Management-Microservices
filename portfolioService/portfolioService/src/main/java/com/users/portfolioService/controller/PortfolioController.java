package com.users.portfolioService.controller;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.users.portfolioService.dtos.BuyStockRequest;
import com.users.portfolioService.dtos.PortfolioResponse;
import com.users.portfolioService.dtos.PortfolioStockDTO;
import com.users.portfolioService.dtos.PortfolioValuationDTO;
import com.users.portfolioService.dtos.SellStockRequest;
import com.users.portfolioService.service.PortfolioService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

	@Autowired
	private PortfolioService service;

	private final String jwtSecret = "my-super-secret-key-for-jwt-signing-my-super-secret-key";

	private String extractEmailFromToken(String token) {
		Key key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	@PostMapping("/buy")
	public ResponseEntity<Void> buyStock(@RequestHeader("Authorization") String token,
			@RequestBody BuyStockRequest request) {
		service.buyStock(extractEmailFromToken(token.replace("Bearer ", "")), request);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/sell")
	public ResponseEntity<Void> sellStock(@RequestHeader("Authorization") String token,
			@RequestBody SellStockRequest request) {
		service.sellStock(extractEmailFromToken(token.replace("Bearer ", "")), request);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/view")
	public ResponseEntity<List<PortfolioResponse>> getPortfolio(@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.getPortfolio(extractEmailFromToken(token.replace("Bearer ", ""))));
	}

	@GetMapping("/value")
	public ResponseEntity<PortfolioValuationDTO> getPortfolioValue(@RequestHeader("Authorization") String token) {
	    String email = service.extractEmailFromToken(token.replace("Bearer ", ""));
	    return ResponseEntity.ok(service.calculatePortfolioSummary(email));
	}
	
	@GetMapping("/holdings")
    public ResponseEntity<List<PortfolioStockDTO>> getPortfolioHoldings(@RequestHeader("Authorization") String token) {
        String email = service.extractEmailFromToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(service.getPortfolioStockDetails(email));
    }
	
}
