/**
 * 
 */
package com.wxxr.mobile.stock.app.v2.bean;

/**
 * @author wangxuyang
 *
 */
public class TradingAccountMenuItem extends BaseMenuItem {
	private String acctId;//交易盘Id
	private String maxHoldStockName;//最大持股名称
	private Long income;//收益 
	private Double incomeRate;//收益率
	private String status;//可买，可卖，已结算
	public String getAcctId() {
		return acctId;
	}
	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}
	public String getMaxHoldStockName() {
		return maxHoldStockName;
	}
	public void setMaxHoldStockName(String maxHoldStockName) {
		this.maxHoldStockName = maxHoldStockName;
	}
	public Long getIncome() {
		return income;
	}
	public void setIncome(Long income) {
		this.income = income;
	}
	public Double getIncomeRate() {
		return incomeRate;
	}
	public void setIncomeRate(Double incomeRate) {
		this.incomeRate = incomeRate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "TradingAccountMenuItem [acctId=" + acctId
				+ ", maxHoldStockName=" + maxHoldStockName + ", income="
				+ income + ", incomeRate=" + incomeRate + ", status=" + status
				+ ", title=" + title + ", date=" + date + ", type=" + type
				+ "]";
	}
	
	
	
	
}
