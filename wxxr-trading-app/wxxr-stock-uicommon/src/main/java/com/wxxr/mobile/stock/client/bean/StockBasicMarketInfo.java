/**
 * 
 */
package com.wxxr.mobile.stock.client.bean;

/**
 * @author wangxuyang
 *
 */
public class StockBasicMarketInfo extends Stock{
	private String currentTime;
	private float lastDayPrice;//昨收价
	private float todayInitPrice;//今开价
	private float currentPrice;
	private float highestPrice;
	private float lowestPrice;
	public String getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
	public float getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(float currentPrice) {
		this.currentPrice = currentPrice;
	}
	public float getHighestPrice() {
		return highestPrice;
	}
	public void setHighestPrice(float highestPrice) {
		this.highestPrice = highestPrice;
	}
	public float getLowestPrice() {
		return lowestPrice;
	}
	public void setLowestPrice(float lowestPrice) {
		this.lowestPrice = lowestPrice;
	}
	public float getLastDayPrice() {
		return lastDayPrice;
	}
	public void setLastDayPrice(float lastDayPrice) {
		this.lastDayPrice = lastDayPrice;
	}
	public float getTodayInitPrice() {
		return todayInitPrice;
	}
	public void setTodayInitPrice(float todayInitPrice) {
		this.todayInitPrice = todayInitPrice;
	}
	
	

}
