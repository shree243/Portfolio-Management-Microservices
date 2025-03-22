package com.users.portfolioService.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "portfolio_entries")
public class PortfolioEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String stockSymbol;

	private Integer quantity;
	private Integer soldQuantity;
	private BigDecimal buyPrice;
	private BigDecimal sellPrice;
	private BigDecimal totalInvestment;
	private BigDecimal totalProfitOrLoss;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public PortfolioEntry() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PortfolioEntry(Long id, String email, String stockSymbol, Integer quantity, Integer soldQuantity,
			BigDecimal buyPrice, BigDecimal sellPrice, BigDecimal totalInvestment, BigDecimal totalProfitOrLoss,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.email = email;
		this.stockSymbol = stockSymbol;
		this.quantity = quantity;
		this.soldQuantity = soldQuantity;
		this.buyPrice = buyPrice;
		this.sellPrice = sellPrice;
		this.totalInvestment = totalInvestment;
		this.totalProfitOrLoss = totalProfitOrLoss;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the stockSymbol
	 */
	public String getStockSymbol() {
		return stockSymbol;
	}

	/**
	 * @param stockSymbol the stockSymbol to set
	 */
	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the soldQuantity
	 */
	public Integer getSoldQuantity() {
		return soldQuantity;
	}

	/**
	 * @param soldQuantity the soldQuantity to set
	 */
	public void setSoldQuantity(Integer soldQuantity) {
		this.soldQuantity = soldQuantity;
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
	 * @return the sellPrice
	 */
	public BigDecimal getSellPrice() {
		return sellPrice;
	}

	/**
	 * @param sellPrice the sellPrice to set
	 */
	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

	/**
	 * @return the totalInvestment
	 */
	public BigDecimal getTotalInvestment() {
		return totalInvestment;
	}

	/**
	 * @param totalInvestment the totalInvestment to set
	 */
	public void setTotalInvestment(BigDecimal totalInvestment) {
		this.totalInvestment = totalInvestment;
	}

	/**
	 * @return the totalProfitOrLoss
	 */
	public BigDecimal getTotalProfitOrLoss() {
		return totalProfitOrLoss;
	}

	/**
	 * @param totalProfitOrLoss the totalProfitOrLoss to set
	 */
	public void setTotalProfitOrLoss(BigDecimal totalProfitOrLoss) {
		this.totalProfitOrLoss = totalProfitOrLoss;
	}

	/**
	 * @return the createdAt
	 */
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the updatedAt
	 */
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}
