package com.wxxr.stock.trading.ejb.api;


import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "GainVO")
public class GainVO implements Serializable{

	private static final long serialVersionUID = 7805747658579008536L;
	/**最大持股代码*/
	@XmlElement(name = "maxStockCode")
	private String maxStockCode;
	/**最大持股名称*/
	@XmlElement(name = "maxStockName")
	private String maxStockName;
	/**最大持股市场*/
	@XmlElement(name = "maxStockMarket")
	private String maxStockMarket;
	/**现金收益*/
	@XmlElement(name = "userGain")
	private Long userGain;
	/**结算时间*/
	@XmlElement(name = "closeTime")
	private String closeTime;
	/**交易盘类型*/
	@XmlElement(name = "virtual")
	private boolean virtual; 
	/**交易盘唯一标识*/
	@XmlElement(name = "tradingAccountId")
	private Long tradingAccountId;
	/**交易盘额度*/
	@XmlElement(name = "sum")
	private Long sum;
	
	/**交易盘操盘利润,我的交易记录*/
	private Long totalGain;
	
	/**1:T日交易盘，0：T+1交易盘*/
	private int status;
	/**交易盘状态（完结/未完结）*/
	private String over;
	
	
	
	public String getMaxStockCode() {
		return maxStockCode;
	}
	public void setMaxStockCode(String maxStockCode) {
		this.maxStockCode = maxStockCode;
	}
	
	public String getMaxStockName() {
		return maxStockName;
	}
	public void setMaxStockName(String maxStockName) {
		this.maxStockName = maxStockName;
	}
	
	public Long getUserGain() {
		return userGain;
	}
	public void setUserGain(Long userGain) {
		this.userGain = userGain;
	}
	
	public String getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}
	
	public boolean isVirtual() {
		return virtual;
	}
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}
	
	public Long getTradingAccountId() {
		return tradingAccountId;
	}
	public void setTradingAccountId(Long tradingAccountId) {
		this.tradingAccountId = tradingAccountId;
	}
	
	public Long getSum() {
		return sum;
	}
	public void setSum(Long sum) {
		this.sum = sum;
	}
	
	/**
	 * @return the maxStockMarket
	 */
	
	public String getMaxStockMarket() {
		return maxStockMarket;
	}
	
	/**
	 * @param maxStockMarket the maxStockMarket to set
	 */
	public void setMaxStockMarket(String maxStockMarket) {
		this.maxStockMarket = maxStockMarket;
	}
	
	/**
	 * 
	 */
	public Long getTotalGain() {
		return totalGain;
	}
	/**
	 * 
	 * @param totalGain
	 */
	public void setTotalGain(Long totalGain) {
		this.totalGain = totalGain;
	}
	
	/**
	 * @return the status
	 */
	
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the over
	 */
	
	public String getOver() {
		return over;
	}
	/**
	 * @param over the over to set
	 */
	public void setOver(String over) {
		this.over = over;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GainVO [maxStockCode=" + maxStockCode + ", maxStockName=" + maxStockName + ", maxStockMarket=" + maxStockMarket + ", userGain=" + userGain
				+ ", closeTime=" + closeTime + ", virtual=" + virtual + ", tradingAccountId=" + tradingAccountId + ", sum=" + sum + ", totalGain=" + totalGain
				+ ", status=" + status + ", over=" + over + "]";
	}
}
