package com.users.stockService.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockPriceServiceImpl implements StockPriceService {

//    private static final String API_KEY = "M4OUEQMWECSI0P68"; // Replace with your actual API key
//    private static final String API_KEY = "ITB0KMU7FKRKOMX7"; // Replace with your actual API key
//    private static final String BASE_URL = "https://www.alphavantage.co/query";
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    private final List<String> trendingSymbols = Arrays.asList("AAPL", "TSLA", "MSFT", "GOOGL", "AMZN");
//
//    @Override
//    public BigDecimal fetchLivePrice(String symbol) {
//        try {
//            String url = BASE_URL + "?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + API_KEY;
//            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
//            Map<String, String> quote = (Map<String, String>) response.get("Global Quote");
//            return new BigDecimal(quote.get("05. price"));
//        } catch (Exception e) {
//            return BigDecimal.ZERO;
//        }
//    }
//
//    @Override
//    public Map<String, String> fetchAllLivePrices() {
//        Map<String, String> prices = new HashMap<>();
//        for (String symbol : trendingSymbols) {
//            try {
//                BigDecimal price = fetchLivePrice(symbol);
//                prices.put(symbol, price.toPlainString());
//            } catch (Exception e) {
//                prices.put(symbol, "0.00");
//            }
//        }
//        return prices;
//    }
//
//    @Override
//    public List<String> getAvailableSymbols() {
//        return trendingSymbols;
//    }
//
//    @Override
//    public List<Map<String, String>> searchStocks(String query) {
//        String url = BASE_URL + "?function=SYMBOL_SEARCH&keywords=" + query + "&apikey=" + API_KEY;
//        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
//        return (List<Map<String, String>>) response.get("bestMatches");
//    }
//
//    @Override
//    public Map<String, String> getStockDetails(String symbol) {
//        String url = BASE_URL + "?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + API_KEY;
//        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
//        return (Map<String, String>) response.get("Global Quote");
//    }
//
//    @Override
//    public Map<String, Object> getStockHistory(String symbol) {
//        String url = BASE_URL + "?function=TIME_SERIES_DAILY&symbol=" + symbol + "&apikey=" + API_KEY;
//        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
//        return response;
//    }
//
//    @Override
//    public List<String> getTrendingStocks() {
//        return trendingSymbols;
//    }

	private static final String API_KEY = "cvfbc99r01qtu9s3scogcvfbc99r01qtu9s3scp0";
	private static final String BASE_URL = "https://finnhub.io/api/v1";

	private final RestTemplate restTemplate = new RestTemplate();

	private final List<String> trendingSymbols = Arrays.asList("AAPL", "TSLA", "MSFT", "GOOGL", "AMZN");

	@Override
	public BigDecimal fetchLivePrice(String symbol) {
		try {
			String url = BASE_URL + "/quote?symbol=" + symbol + "&token=" + API_KEY;
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
		String url = BASE_URL + "/search?q=" + query + "&token=" + API_KEY;
		Map<String, Object> response = restTemplate.getForObject(url, Map.class);
		return (List<Map<String, String>>) response.get("result");
	}

	@Override
	public Map<String, String> getStockDetails(String symbol) {
		String url = BASE_URL + "/quote?symbol=" + symbol + "&token=" + API_KEY;
		Map<String, Object> response = restTemplate.getForObject(url, Map.class);
		Map<String, String> details = new HashMap<>();
		details.put("current", String.valueOf(response.get("c")));
		details.put("high", String.valueOf(response.get("h")));
		details.put("low", String.valueOf(response.get("l")));
		details.put("open", String.valueOf(response.get("o")));
		details.put("previousClose", String.valueOf(response.get("pc")));
		return details;
	}

	@Override
	public Map<String, Object> getStockHistory(String symbol) {
		// Finnhub requires UNIX timestamps, can be enhanced with date ranges later
		long to = Instant.now().getEpochSecond();
		long from = Instant.now().minus(25, ChronoUnit.DAYS).getEpochSecond();

		String url = BASE_URL + "/stock/candle?symbol=" + symbol +
		             "&resolution=D&from=" + from + "&to=" + to +
		             "&token=" + API_KEY;
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

//	private static final String API_KEY = "your_iex_cloud_api_key_here";
//	private static final String BASE_URL = "https://cloud.iexapis.com/stable";
//
//	@Override
//	public BigDecimal fetchLivePrice(String symbol) {
//		try {
//			String url = BASE_URL + "/stock/" + symbol + "/quote?token=" + API_KEY;
//			Map<String, Object> response = restTemplate.getForObject(url, Map.class);
//			return new BigDecimal(String.valueOf(response.get("latestPrice")));
//		} catch (Exception e) {
//			return BigDecimal.ZERO;
//		}
//	}
//
//	@Override
//	public List<Map<String, String>> searchStocks(String query) {
//		String url = BASE_URL + "/search/" + query + "?token=" + API_KEY;
//		List<Map<String, String>> response = restTemplate.getForObject(url, List.class);
//		return response;
//	}
//
//	@Override
//	public Map<String, String> getStockDetails(String symbol) {
//		String url = BASE_URL + "/stock/" + symbol + "/quote?token=" + API_KEY;
//		Map<String, Object> response = restTemplate.getForObject(url, Map.class);
//		Map<String, String> details = new HashMap<>();
//		details.put("current", String.valueOf(response.get("latestPrice")));
//		details.put("high", String.valueOf(response.get("high")));
//		details.put("low", String.valueOf(response.get("low")));
//		details.put("open", String.valueOf(response.get("open")));
//		details.put("previousClose", String.valueOf(response.get("previousClose")));
//		return details;
//	}
//
//	@Override
//	public Map<String, Object> getStockHistory(String symbol) {
//		String url = BASE_URL + "/stock/" + symbol + "/chart/1m?token=" + API_KEY;
//		List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
//		Map<String, Object> history = new HashMap<>();
//		history.put("data", response);
//		return history;
//	}
//
//	@Override
//	public Map<String, String> fetchAllLivePrices() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<String> getAvailableSymbols() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<String> getTrendingStocks() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
