package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="STOCK")
public class StockVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String stockCode;
	private String stockName;
	private String tradeDate;
	private String highs;
	private int achieveDays;
	
	@XmlElement
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	@XmlElement
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	@XmlElement
	public String getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}
	@XmlElement
	public String getHighs() {
		return highs;
	}
	public void setHighs(String highs) {
		this.highs = highs;
	}
	@XmlElement
	public int getAchieveDays() {
		return achieveDays;
	}
	public void setAchieveDays(int achieveDays) {
		this.achieveDays = achieveDays;
	}
	
}
