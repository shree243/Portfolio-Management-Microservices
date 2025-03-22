package com.users.stockService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.users.stockService.service.StockPriceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stock")
public class StockController {

	@Autowired
	private StockPriceService stockPriceService;

	@GetMapping("/price/{symbol}")
	public ResponseEntity<BigDecimal> getLivePrice(@PathVariable String symbol) {
		BigDecimal ans = stockPriceService.fetchLivePrice(symbol);
		System.out.println(ans + " ans -> ");
		return ResponseEntity.ok(ans);
//		return ResponseEntity.ok(stockPriceService.fetchLivePrice(symbol));
	}

	@GetMapping("/symbols")
	public ResponseEntity<List<String>> getAvailableSymbols() {
		List<String> ll = stockPriceService.getAvailableSymbols();
		System.out.println(ll + " ans -> ");
//		return ResponseEntity.ok(stockPriceService.getAvailableSymbols());
		return ResponseEntity.ok(ll);
	}

	@GetMapping("/search")
	public ResponseEntity<List<Map<String, String>>> searchStocks(@RequestParam String query) {
		List<Map<String, String>> jh = stockPriceService.searchStocks(query);
		System.out.println(jh + " ans -> ");
//		return ResponseEntity.ok(stockPriceService.searchStocks(query));
		return ResponseEntity.ok(jh);
	}

	@GetMapping("/details/{symbol}")
	public ResponseEntity<Map<String, String>> getStockDetails(@PathVariable String symbol) {
		Map<String, String> ss = stockPriceService.getStockDetails(symbol);
		System.out.println(ss + " ans -> ");
		return ResponseEntity.ok(ss);
//		return ResponseEntity.ok(stockPriceService.getStockDetails(symbol));
	}

	@GetMapping("/history/{symbol}")
	public ResponseEntity<Map<String, Object>> getStockHistory(@PathVariable String symbol) {
		return ResponseEntity.ok(stockPriceService.getStockHistory(symbol));
	}

	@GetMapping("/trending")
	public ResponseEntity<List<String>> getTrendingStocks() {
		return ResponseEntity.ok(stockPriceService.getTrendingStocks());
	}
}