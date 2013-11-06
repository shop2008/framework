/**
 * 
 */
package com.wxxr.mobile.stock.client.bean;


/**
 * 交易盘
 * @author wangxuyang
 *
 */
public class TradingAccount {
	private Long id;
	private int type;//交易盘类型  0-模拟盘；1-实盘
	private String stockCode;//最大持股代码
	private String stockName;//最大持股名称
	private String createDate;//申请日期
	private String endDate;//截止日期
	private float initCredit;//额度（申请资金）
	private float income;//总收益
	private int status;//状态 0-未结算 ； 1-已结算
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
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
