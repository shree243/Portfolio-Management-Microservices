package com.users.portfolioService.dtos;


import java.math.BigDecimal;

public class PortfolioValuationDTO {
    private BigDecimal investedValue;
    private BigDecimal currentValue;
    private BigDecimal profit;
    private BigDecimal percentageGrowth;

    public BigDecimal getInvestedValue() {
        return investedValue;
    }

    public void setInvestedValue(BigDecimal investedValue) {
        this.investedValue = investedValue;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getPercentageGrowth() {
        return percentageGrowth;
    }

    public void setPercentageGrowth(BigDecimal percentageGrowth) {
        this.percentageGrowth = percentageGrowth;
    }
}