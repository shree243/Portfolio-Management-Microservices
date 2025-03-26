package com.users.portfolioService.service;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "stock-service")
public interface StockClient {
	@GetMapping("/api/stocks/price/{symbol}")
	BigDecimal getLivePrice(@PathVariable String symbol);
}
