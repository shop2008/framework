package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="STOCK")
public class StockVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "stockCode")
	private String stockCode;
	@XmlElement(name = "stockName")
	private String stockName;
	@XmlElement(name = "tradeDate")
	private String tradeDate;
	@XmlElement(name = "highs")
	private String highs;
	@XmlElement(name = "achieveDays")
	private int achieveDays;
	
	
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	
	public String getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}
	
	public String getHighs() {
		return highs;
	}
	public void setHighs(String highs) {
		this.highs = highs;
	}
	
	public int getAchieveDays() {
		return achieveDays;
	}
	public void setAchieveDays(int achieveDays) {
		this.achieveDays = achieveDays;
	}
	
}
