package com.wxxr.mobile.stock.client.bean;

public class TradeRecordEntity {

	/**
	 * 股票名称
	 */
	private String stockName;
	
	/**
	 * 股票代码
	 */
	private String stockCode;
	
	/**
	 * 额度
	 */
	private String tradeAmount;
	
	/**
	 * 盈亏
	 */
	private String tradeProfit;
	
	
	/**
	 * 交易日期
	 */
	private String tradeDate;
	
	/**
	 * 交易类型
	 */
	private String tradeType;

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(String tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public String getTradeProfit() {
		return tradeProfit;
	}

	public void setTradeProfit(String tradeProfit) {
		this.tradeProfit = tradeProfit;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
}
