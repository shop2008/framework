package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "StrongStock")
public class StrongStockVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String stockCode;
	private String stockName;
	private String tradedate;
	private String recentHigh;
	private String dayHighs;
	private String dayChange;
	private String twentyDaysChange;
	private int achieveDays;
	private int live;
	
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
	public String getTradedate() {
		return tradedate;
	}
	public void setTradedate(String tradedate) {
		this.tradedate = tradedate;
	}
	
	@XmlElement
	public String getDayHighs() {
		return dayHighs;
	}
	
	public void setDayHighs(String dayHighs) {
		this.dayHighs = dayHighs;
	}
	
	@XmlElement
	public String getRecentHigh() {
		return recentHigh;
	}
	
	public void setRecentHigh(String recentHigh) {
		this.recentHigh = recentHigh;
	}
	
	@XmlElement
	public String getDayChange() {
		return dayChange;
	}
	public void setDayChange(String dayChange) {
		this.dayChange = dayChange;
	}
	@XmlElement
	public String getTwentyDaysChange() {
		return twentyDaysChange;
	}
	public void setTwentyDaysChange(String twentyDaysChange) {
		this.twentyDaysChange = twentyDaysChange;
	}
	@XmlElement
	public int getAchieveDays() {
		return achieveDays;
	}
	public void setAchieveDays(int achieveDays) {
		this.achieveDays = achieveDays;
	}
	@XmlElement
	public int getLive() {
		return live;
	}
	public void setLive(int live) {
		this.live = live;
	}
	
	
	
}
