package com.users.portfolioService.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.users.portfolioService.dtos.BuyStockRequest;
import com.users.portfolioService.dtos.PortfolioResponse;
import com.users.portfolioService.dtos.PortfolioStockDTO;
import com.users.portfolioService.dtos.PortfolioValuationDTO;
import com.users.portfolioService.dtos.SellStockRequest;
import com.users.portfolioService.service.PortfolioServiceImpl;

@SpringBootTest
public class PortfolioControllerTest {

	@Mock
	private PortfolioServiceImpl portfolioService;

	@InjectMocks
	private PortfolioController controller;

	private String token;
	private BuyStockRequest buyRequest;
	private SellStockRequest sellRequest;
	private PortfolioValuationDTO valuationDTO;
	private List<PortfolioResponse> portfolioList;
	private List<PortfolioStockDTO> holdingsList;

	@BeforeEach
	void setup() {
		token = "dummy-login-token";

		buyRequest = new BuyStockRequest();
		buyRequest.setStockSymbol("AAPL");
		buyRequest.setQuantity(10);
		buyRequest.setPrice(new BigDecimal("150"));

		sellRequest = new SellStockRequest();
		sellRequest.setStockSymbol("AAPL");
		sellRequest.setQuantity(5);
		sellRequest.setPrice(new BigDecimal("155"));

		valuationDTO = new PortfolioValuationDTO();
		valuationDTO.setInvestedValue(new BigDecimal("10000"));
		valuationDTO.setCurrentValue(new BigDecimal("12345.67"));
		valuationDTO.setProfit(new BigDecimal("2345.67"));
		valuationDTO.setPercentageGrowth(new BigDecimal("23.45"));

		portfolioList = new ArrayList<>();
		portfolioList.add(new PortfolioResponse("AAPL", 10, new BigDecimal("150.00"), new BigDecimal("155.00"),
				new BigDecimal("50.00")));

		holdingsList = new ArrayList<>();
		holdingsList.add(new PortfolioStockDTO("AAPL", 10));
	}

//	Testing of buy controller method should return ok status 
//	@Test
	void buyStock_ShouldReturnOk() {
		when(portfolioService.extractEmailFromToken(anyString())).thenReturn("test@example.com");
		doNothing().when(portfolioService).buyStock(anyString(), any(BuyStockRequest.class));
		ResponseEntity<Void> response = controller.buyStock(token, buyRequest);
		System.out.println(response + " <-- response");
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}


//	Testing of Sell controller method should return ok status 
//	@Test
	void sellStock_ShouldReturnOk() {
		when(portfolioService.extractEmailFromToken(anyString())).thenReturn("test@example.com");
		doNothing().when(portfolioService).sellStock(anyString(), any(SellStockRequest.class));
		ResponseEntity<Void> response = controller.sellStock(token, sellRequest);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

//	Testing of getPortfolio method should return list of all stocks
//    @Test
	void getPortfolio_ShouldReturnList() {
		when(portfolioService.extractEmailFromToken(anyString())).thenReturn("test@example.com");
		when(portfolioService.getPortfolio(anyString())).thenReturn(portfolioList);
		ResponseEntity<List<PortfolioResponse>> response = controller.getPortfolio(token);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(portfolioList.size(), response.getBody().size());
	}

//	Testing of getPortfolio values should return invested value
//    @Test
	void getPortfolioValue_ShouldReturnSummary() {
		when(portfolioService.extractEmailFromToken(anyString())).thenReturn("test@example.com");
		when(portfolioService.calculatePortfolioSummary(anyString())).thenReturn(valuationDTO);
		ResponseEntity<PortfolioValuationDTO> response = controller.getPortfolioValue(token);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(valuationDTO.getInvestedValue(), response.getBody().getInvestedValue());
	}
	
//	Testing of getPortfolio holding stocks should return holding size
//    @Test
	void getPortfolioHoldings_ShouldReturnHoldings() {
		when(portfolioService.extractEmailFromToken(anyString())).thenReturn("test@example.com");
		when(portfolioService.getPortfolioStockDetails(anyString())).thenReturn(holdingsList);
		ResponseEntity<List<PortfolioStockDTO>> response = controller.getPortfolioHoldings(token);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(holdingsList.size(), response.getBody().size());
	}

}
