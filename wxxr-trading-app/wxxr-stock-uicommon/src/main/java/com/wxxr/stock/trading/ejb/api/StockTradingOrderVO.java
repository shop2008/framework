/**
 * 
 */
package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * @author wangyan
 *
 */
@XmlRootElement(name = "tradingOrder")
public class StockTradingOrderVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6528384969149357528L;
	@XmlElement(name = "id")
	private Long id;//Id
	@XmlElement(name = "stockCode")
	private String stockCode;
	@XmlElement(name = "marketCode")
	private String marketCode;
	@XmlElement(name = "currentPirce")
	private Long currentPirce;
	@XmlElement(name = "changeRate")
	private Long changeRate;
	@XmlElement(name = "buy")
	private Long buy;
	@XmlElement(name = "amount")
	private Long amount;
	@XmlElement(name = "gain")
	private Long gain;
	@XmlElement(name = "gainRate")
	private Long gainRate;
	@XmlElement(name = "status")
	private String status;
	
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
	 * @return the stockCode
	 */
	
	public String getStockCode() {
		return stockCode;
	}
	/**
	 * @param stockCode the stockCode to set
	 */
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	/**
	 * @return the marketCode
	 */
	
	public String getMarketCode() {
		return marketCode;
	}
	/**
	 * @param marketCode the marketCode to set
	 */
	public void setMarketCode(String marketCode) {
		this.marketCode = marketCode;
	}
	/**
	 * @return the currentPirce
	 */
	
	public Long getCurrentPirce() {
		return currentPirce;
	}
	/**
	 * @param currentPirce the currentPirce to set
	 */
	public void setCurrentPirce(Long currentPirce) {
		this.currentPirce = currentPirce;
	}
	/**
	 * @return the changeRate
	 */
	
	public Long getChangeRate() {
		return changeRate;
	}
	/**
	 * @param changeRate the changeRate to set
	 */
	public void setChangeRate(Long changeRate) {
		this.changeRate = changeRate;
	}
	/**
	 * @return the buy
	 */
	
	public Long getBuy() {
		return buy;
	}
	/**
	 * @param buy the buy to set
	 */
	public void setBuy(Long buy) {
		this.buy = buy;
	}
	/**
	 * @return the amount
	 */
	
	public Long getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	/**
	 * @return the gain
	 */
	
	public Long getGain() {
		return gain;
	}
	/**
	 * @param gain the gain to set
	 */
	public void setGain(Long gain) {
		this.gain = gain;
	}
	/**
	 * @return the gainRate
	 */
	
	public Long getGainRate() {
		return gainRate;
	}
	/**
	 * @param gainRate the gainRate to set
	 */
	public void setGainRate(Long gainRate) {
		this.gainRate = gainRate;
	}
	
	/**
	 * @return the status
	 */
	
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StockTradingOrderVO [id=" + id + ", stockCode=" + stockCode + ", marketCode=" + marketCode
				+ ", currentPirce=" + currentPirce + ", changeRate=" + changeRate + ", buy=" + buy + ", amount="
				+ amount + ", gain=" + gain + ", gainRate=" + gainRate + ", status=" + status + "]";
	}
	
	
}
