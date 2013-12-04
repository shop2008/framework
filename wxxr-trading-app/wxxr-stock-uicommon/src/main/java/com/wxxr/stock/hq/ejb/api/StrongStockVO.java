package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "StrongStock")
public class StrongStockVO implements Serializable{

	private static final long serialVersionUID = 1L;
	@XmlElement(name = "stockCode")
	private String stockCode;
	@XmlElement(name = "stockName")
	private String stockName;
	@XmlElement(name = "tradedate")
	private String tradedate;
	@XmlElement(name = "recentHigh")
	private String recentHigh;
	@XmlElement(name = "dayHighs")
	private String dayHighs;
	@XmlElement(name = "dayChange")
	private String dayChange;
	@XmlElement(name = "twentyDaysChange")
	private String twentyDaysChange;
	@XmlElement(name = "achieveDays")
	private int achieveDays;
	@XmlElement(name = "live")
	private int live;
	
	
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
	
	public String getTradedate() {
		return tradedate;
	}
	public void setTradedate(String tradedate) {
		this.tradedate = tradedate;
	}
	
	
	public String getDayHighs() {
		return dayHighs;
	}
	
	public void setDayHighs(String dayHighs) {
		this.dayHighs = dayHighs;
	}
	
	
	public String getRecentHigh() {
		return recentHigh;
	}
	
	public void setRecentHigh(String recentHigh) {
		this.recentHigh = recentHigh;
	}
	
	
	public String getDayChange() {
		return dayChange;
	}
	public void setDayChange(String dayChange) {
		this.dayChange = dayChange;
	}
	
	public String getTwentyDaysChange() {
		return twentyDaysChange;
	}
	public void setTwentyDaysChange(String twentyDaysChange) {
		this.twentyDaysChange = twentyDaysChange;
	}
	
	public int getAchieveDays() {
		return achieveDays;
	}
	public void setAchieveDays(int achieveDays) {
		this.achieveDays = achieveDays;
	}
	
	public int getLive() {
		return live;
	}
	public void setLive(int live) {
		this.live = live;
	}
	
	
	
}
