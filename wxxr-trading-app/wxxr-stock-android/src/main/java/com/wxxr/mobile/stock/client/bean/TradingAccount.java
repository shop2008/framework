/**
 * 
 */
package com.wxxr.mobile.stock.client.bean;

/**
 * @author wangxuyang
 *
 */
public class TradingAccount {
	private Long id;
	private String stockCode;//最大持股代码
	private String stockName;//最大持股名称
	private float initCredit;//额度（申请资金）
	private float income;//总收益
	private boolean isJieSuan;//是否已结算
	private float available;//可用资金
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public float getInitCredit() {
		return initCredit;
	}
	public void setInitCredit(float initCredit) {
		this.initCredit = initCredit;
	}
	public float getIncome() {
		return income;
	}
	public void setIncome(float income) {
		this.income = income;
	}
	public float getAvailable() {
		return available;
	}
	public void setAvailable(float available) {
		this.available = available;
	}
	public boolean isJieSuan() {
		return isJieSuan;
	}
	public void setJieSuan(boolean isJieSuan) {
		this.isJieSuan = isJieSuan;
	}
	

}
