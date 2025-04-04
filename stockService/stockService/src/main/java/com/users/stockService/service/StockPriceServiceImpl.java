package com.users.stockService.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.users.stockService.controller.dto.CandlestickDTO;

@Service
public class StockPriceServiceImpl implements StockPriceService {

	private static final String FINHUB_BASE_URL = "https://finnhub.io/api/v1";

	@Value("${api.alphavantage.key}")
	private String alphaVantageKey;

	@Value("${api.finnhub.key}")
	private String finnhubKey;

	private final RestTemplate restTemplate = new RestTemplate();

	private final List<String> trendingSymbols = Arrays.asList("AAPL", "TSLA", "MSFT", "GOOGL", "AMZN");

	@Override
	public BigDecimal fetchLivePrice(String symbol) {
		try {
			String url = FINHUB_BASE_URL + "/quote?symbol=" + symbol + "&token=" + finnhubKey;
			Map<String, Object> response = restTemplate.getForObject(url, Map.class);
			return new BigDecimal(String.valueOf(response.get("c"))); // current price
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

	@Override
	public Map<String, String> fetchAllLivePrices() {
		Map<String, String> prices = new HashMap<>();
		for (String symbol : trendingSymbols) {
			try {
				BigDecimal price = fetchLivePrice(symbol);
				prices.put(symbol, price.toPlainString());
			} catch (Exception e) {
				prices.put(symbol, "0.00");
			}
		}
		return prices;
	}

	@Override
	public List<String> getAvailableSymbols() {
		return trendingSymbols;
	}

	@Override
	public List<Map<String, String>> searchStocks(String query) {
		String url = FINHUB_BASE_URL + "/search?q=" + query + "&token=" + finnhubKey;
		Map<String, Object> response = restTemplate.getForObject(url, Map.class);
		return (List<Map<String, String>>) response.get("result");
	}

	@Override
	public Map<String, String> getStockDetails(String symbol) {
		String url = FINHUB_BASE_URL + "/quote?symbol=" + symbol + "&token=" + finnhubKey;
		Map<String, Object> response = restTemplate.getForObject(url, Map.class);
		Map<String, String> details = new HashMap<>();
		details.put("current", String.valueOf(response.get("c")));
		details.put("high", String.valueOf(response.get("h")));
		details.put("low", String.valueOf(response.get("l")));
		details.put("open", String.valueOf(response.get("o")));
		details.put("stockSymbol", symbol);
		details.put("previousClose", String.valueOf(response.get("pc")));
		return details;
	}

	@Override
	public Map<String, Object> getStockHistory(String symbol) {
		long to = Instant.now().getEpochSecond();
		long from = Instant.now().minus(25, ChronoUnit.DAYS).getEpochSecond();

		String url = FINHUB_BASE_URL + "/stock/candle?symbol=" + symbol + "&resolution=D&from=" + from + "&to=" + to
				+ "&token=" + finnhubKey;
		Map<String, Object> response = restTemplate.getForObject(url, Map.class);
		if (response.containsKey("s") && "no_data".equals(response.get("s"))) {
			return Map.of("message", "No historical data available for this symbol.");
		}
		return response;

	}

	@Override
	public List<String> getTrendingStocks() {
		return trendingSymbols;
	}

	@Override
	public List<CandlestickDTO> getCandlestickData(String symbol) {
		String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY" + "&symbol=" + symbol.toUpperCase()
				+ "&outputsize=full" + "&apikey=" + alphaVantageKey;

		try {
			String response = restTemplate.getForObject(url, String.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response);
			JsonNode timeSeries = root.path("Time Series (Daily)");

			if (timeSeries.isMissingNode()) {
				throw new RuntimeException("Invalid API response for symbol: " + symbol);
			}

			List<CandlestickDTO> candles = new ArrayList<>();

			Iterator<String> dates = timeSeries.fieldNames();
			while (dates.hasNext()) {
				String dateStr = dates.next();
				JsonNode day = timeSeries.get(dateStr);

				candles.add(new CandlestickDTO(LocalDate.parse(dateStr), new BigDecimal(day.get("1. open").asText()),
						new BigDecimal(day.get("2. high").asText()), new BigDecimal(day.get("3. low").asText()),
						new BigDecimal(day.get("4. close").asText())));
			}

			candles.sort(Comparator.comparing(CandlestickDTO::getDate));

			LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
			return candles.stream().filter(c -> c.getDate().isAfter(sixMonthsAgo)).collect(Collectors.toList());

		} catch (Exception e) {
			throw new RuntimeException("Failed to fetch candlestick data", e);
		}
	}
}
