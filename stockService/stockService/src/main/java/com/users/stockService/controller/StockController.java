package com.users.stockService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.users.stockService.controller.dto.CandlestickDTO;
import com.users.stockService.service.StockPriceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Tag(name = "Stock Controller",
description = "to fetch the stock live prices from https://alphavantage.co and https://finnhub.io websites")
@RestController
@RequestMapping("/api/stocks")
public class StockController {

	@Autowired
	private StockPriceService stockPriceService;

	@Operation(summary = "To get live stock price",
		      description = " @param request contains stock symbol"
		      		+ "* @return stock live price if stocks available")
	@GetMapping("/price/{symbol}")
	public ResponseEntity<BigDecimal> getLivePrice(@PathVariable String symbol) {
		BigDecimal price = stockPriceService.fetchLivePrice(symbol);
		System.out.println(price + " price -> ");
		return ResponseEntity.ok(price);
	}

	@Operation(summary = "To get available stock symbols",
		      description = " @param request does not contains anything"
		      		+ "* @return list of stocks symbols")
	@GetMapping("/symbols")
	public ResponseEntity<List<String>> getAvailableSymbols() {
		List<String> symbols = stockPriceService.getAvailableSymbols();
		System.out.println(symbols + " <- ");
		return ResponseEntity.ok(symbols);
	}

	@Operation(summary = "To get available stock details in query",
		      description = " @param request contains stock name"
		      		+ "* @return list of key value pair of stock details.")
	@GetMapping("/search")
	public ResponseEntity<List<Map<String, String>>> searchStocks(@RequestParam String query) {
		List<Map<String, String>> stocks = stockPriceService.searchStocks(query);
		System.out.println(stocks + " stocks -> ");
		return ResponseEntity.ok(stocks);
	}

	@Operation(summary = "To get available stock details",
		      description = " @param request contains stock name"
		      		+ "* @return list of key value pair of stock details.")
	@GetMapping("/details/{symbol}")
	public ResponseEntity<Map<String, String>> getStockDetails(@PathVariable String symbol) {
		Map<String, String> stockDetails = stockPriceService.getStockDetails(symbol);
		System.out.println(stockDetails + " ans -> ");
		return ResponseEntity.ok(stockDetails);
	}

	@Operation(summary = "To get available stock history",
		      description = " @param request contains stock name"
		      		+ "* @return list of key value pair of stock details from starting")
	@GetMapping("/history/{symbol}")
	public ResponseEntity<Map<String, Object>> getStockHistory(@PathVariable String symbol) {
		return ResponseEntity.ok(stockPriceService.getStockHistory(symbol));
	}

	@Operation(summary = "To get available trending stocks",
		      description = " @param request dosenot contains anything"
		      		+ "* @return list of trending stock names.")
	@GetMapping("/trending")
	public ResponseEntity<List<String>> getTrendingStocks() {
		return ResponseEntity.ok(stockPriceService.getTrendingStocks());
	}

	@Operation(summary = "To get available stock history",
		      description = " @param request contains stock name"
		      		+ "* @return list of key value pair of stock details for past 6 months")
	@GetMapping("/{symbol}/candles")
	public ResponseEntity<List<CandlestickDTO>> getCandlestickData(@PathVariable String symbol) {
		return ResponseEntity.ok(stockPriceService.getCandlestickData(symbol));
	}
}