/**
 * 
 */
package com.wxxr.mobile.stock.client.bean;

import java.util.Map;

/**
 * @author wangxuyang
 *
 */
public class StockFiveGradeInfo {
	private String stockCode;//股票代码 
	private String stockName;//股票名称
	private Map<Float,Integer> buyInfo;//买盘价量
	private Map<Float,Integer> sellInfo;//卖盘价量
	private String externalAmount;
	private String internalAmount;
	private Float currentPrice;
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public Map<Float, Integer> getBuyInfo() {
		return buyInfo;
	}
	public void setBuyInfo(Map<Float, Integer> buyInfo) {
		this.buyInfo = buyInfo;
	}
	public Map<Float, Integer> getSellInfo() {
		return sellInfo;
	}
	public void setSellInfo(Map<Float, Integer> sellInfo) {
		this.sellInfo = sellInfo;
	}
	public String getExternalAmount() {
		return externalAmount;
	}
	public void setExternalAmount(String externalAmount) {
		this.externalAmount = externalAmount;
	}
	public String getInternalAmount() {
		return internalAmount;
	}
	public void setInternalAmount(String internalAmount) {
		this.internalAmount = internalAmount;
	}
	public Float getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(Float currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	

}
