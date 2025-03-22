package com.users.portfolioService.dtos;

import java.math.BigDecimal;

public class PortfolioStockDTO {

	private String stock;
	private int quantity;
	private BigDecimal buyPrice;
	private BigDecimal currentPrice;
	private BigDecimal currentValue;

	/**
	 * @return the stock
	 */
	public String getStock() {
		return stock;
	}

	/**
	 * @return the currentValue
	 */
	public BigDecimal getCurrentValue() {
		return currentValue;
	}

	/**
	 * @param currentValue the currentValue to set
	 */
	public void setCurrentValue(BigDecimal currentValue) {
		this.currentValue = currentValue;
	}

	/**
	 * @param stock the stock to set
	 */
	public void setStock(String stock) {
		this.stock = stock;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the buyPrice
	 */
	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	/**
	 * @param buyPrice the buyPrice to set
	 */
	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}

	/**
	 * @return the currentPrice
	 */
	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}

	/**
	 * o
	 * 
	 * @param currentPrice the currentPrice to set
	 */
	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}

}
