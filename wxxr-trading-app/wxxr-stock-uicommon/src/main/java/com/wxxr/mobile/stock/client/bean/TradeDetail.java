package com.wxxr.mobile.stock.client.bean;

/**
 * 收支详情
 * @author renwenjie
 */
public class TradeDetail {
	
	/**
	 * 交易类别
	 */
	private String tradeCatagory;
	
	/**
	 * 交易日期
	 */
	private String tradeDate;
	
	/**
	 * 交易额度
	 */
	private String tradeAmount;

	public String getTradeCatagory() {
		return tradeCatagory;
	}

	public void setTradeCatagory(String tradeCatagory) {
		this.tradeCatagory = tradeCatagory;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(String tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
}
