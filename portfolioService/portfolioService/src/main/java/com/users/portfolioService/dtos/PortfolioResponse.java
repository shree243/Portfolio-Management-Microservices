package com.users.portfolioService.dtos;

import java.math.BigDecimal;

public class PortfolioResponse {
	private String stockSymbol;
	private Integer quantity;
	private BigDecimal buyPrice;
	private BigDecimal sellPrice;
	private BigDecimal profitOrLoss;

	public PortfolioResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PortfolioResponse(String stockSymbol, Integer quantity, BigDecimal buyPrice, BigDecimal sellPrice,
			BigDecimal profitOrLoss) {
		super();
		this.stockSymbol = stockSymbol;
		this.quantity = quantity;
		this.buyPrice = buyPrice;
		this.sellPrice = sellPrice;
		this.profitOrLoss = profitOrLoss;
	}

	public String getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}

	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	public BigDecimal getProfitOrLoss() {
		return profitOrLoss;
	}

	public void setProfitOrLoss(BigDecimal profitOrLoss) {
		this.profitOrLoss = profitOrLoss;
	}
}
