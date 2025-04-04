package com.users.portfolioService.controller;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Portfolio Controller", description = "To Buy, Sell te stocks and fetch the portfolio holdings, values and last view the whole portfolio based on user Credentials")
@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

	@Autowired
	private PortfolioService service;
	@Value("${jwt.secret}")
	private String jwtSecret;

	private String extractEmailFromToken(String token) {
		Key key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	@Operation(summary = "To Buy the stocks", description = " @param request contains toke release by Auth Service for particular user"
			+ "* @return Ok status if purchasing the stock is successful")
	@PostMapping("/buy")
	public ResponseEntity<Void> buyStock(@RequestHeader("Authorization") String token,
			@RequestBody BuyStockRequest request) {
		String email = service.extractEmailFromToken(token.replace("Bearer ", ""));
		service.buyStock(email, request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "To Sell the stocks", description = " @param request contains toke release by Auth Service for particular user"
			+ "* @return Ok status if selling the stock is successful")
	@PostMapping("/sell")
	public ResponseEntity<Void> sellStock(@RequestHeader("Authorization") String token,
			@RequestBody SellStockRequest request) {
		String email = service.extractEmailFromToken(token.replace("Bearer ", ""));
		service.sellStock(email, request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "To View the stocks details in portfolio", description = " @param request contains toke release by Auth Service for particular user"
			+ "* @return List of stock details such as buy price, sell price, quantity etc.")
	@GetMapping("/view")
	public ResponseEntity<List<PortfolioResponse>> getPortfolio(@RequestHeader("Authorization") String token) {
		String email = service.extractEmailFromToken(token.replace("Bearer ", ""));
		return ResponseEntity.ok(service.getPortfolio(email));
	}

	@Operation(summary = "To fetch overall portfolio numbers as profit, invested values etc.", description = " @param request contains toke release by Auth Service for particular user"
			+ "* @return contains overall profit, invested value, current value, profit/loss numbers from portfolio.")
	@GetMapping("/value")
	public ResponseEntity<PortfolioValuationDTO> getPortfolioValue(@RequestHeader("Authorization") String token) {
		String email = service.extractEmailFromToken(token.replace("Bearer ", ""));
		return ResponseEntity.ok(service.calculatePortfolioSummary(email));
	}

	@Operation(summary = "To fetch List fo stock details", description = " @param request contains toke release by Auth Service for particular user"
			+ "* @return contains stock symbol, current price, quantity numbers from portfolio.")
	@GetMapping("/holdings")
	public ResponseEntity<List<PortfolioStockDTO>> getPortfolioHoldings(@RequestHeader("Authorization") String token) {
		String email = service.extractEmailFromToken(token.replace("Bearer ", ""));
		return ResponseEntity.ok(service.getPortfolioStockDetails(email));
	}

}
