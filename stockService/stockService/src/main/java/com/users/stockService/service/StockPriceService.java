package com.users.stockService.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface StockPriceService {
	BigDecimal fetchLivePrice(String symbol);

	Map<String, String> fetchAllLivePrices();

	List<String> getAvailableSymbols();

	List<Map<String, String>> searchStocks(String query);

	Map<String, String> getStockDetails(String symbol);

	Map<String, Object> getStockHistory(String symbol);

	List<String> getTrendingStocks();
}
