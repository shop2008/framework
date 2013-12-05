
package com.wxxr.stock.trading.ejb.api;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;




@XmlRootElement(name = "AuditInfoVO")
public class AuditInfoVO{

	/**申请资金*/
	@XmlElement(name = "fund")
	private String fund;
	/**总盈亏率*/
	@XmlElement(name = "plRisk")
	private String plRisk;
	/**盈亏总额（交易盘，除去费用）*/
	@XmlElement(name = "")
	private String totalGain;
	/**玩家实得收益--没有收益时不显示80%*/
	@XmlElement(name = "")
	private String userGain;
	/**补偿交易综合费--总收益<手续费，=总收益（优先补偿)*/
	@XmlElement(name = "")
	private String tradingCost;
	/**账户管理费--没有收益时不显示20%*/
	@XmlElement(name = "")
	private String accountPay;
	/**止损比例*/
	@XmlElement(name = "")
	private String capitalRate;
	/**冻结资金*/
	@XmlElement(name = "")
	private String frozenAmount;
	/**扣减数*/
	@XmlElement(name = "")
	private String payOut;
	/**解冻数量*/
	@XmlElement(name = "")
	private String unfreezeAmount;
	/**买入均价*/
	@XmlElement(name = "")
	private String buyAverage;
	/**卖出均价*/
	@XmlElement(name = "")
	private String sellAverage;
	/**交易结算日期*/
	@XmlElement(name = "")
	private String tradingDate;
	/**交易盘类型*/
	@XmlElement(name = "")
	private boolean virtual;
	/**申请交易盘时间*/
	@XmlElement(name = "")
	private String buyDay;
	/**存续时间*/
	@XmlElement(name = "")
	private String deadline;
	/**手续费*/
	@XmlElement(name = "")
	private String cost;
	/**交易盘编号*/
	@XmlElement(name = "")
	private String id;
	/**交易盘类型*/
	@XmlElement(name = "")
	private String type;
	

	
	
	/**
	 * @return the fund
	 */
	public String getFund() {
		return fund;
	}




	/**
	 * @param fund the fund to set
	 */
	public void setFund(String fund) {
		this.fund = fund;
	}




	/**
	 * @return the plRisk
	 */
	public String getPlRisk() {
		return plRisk;
	}




	/**
	 * @param plRisk the plRisk to set
	 */
	public void setPlRisk(String plRisk) {
		this.plRisk = plRisk;
	}




	/**
	 * @return the totalGain
	 */
	public String getTotalGain() {
		return totalGain;
	}




	/**
	 * @param totalGain the totalGain to set
	 */
	public void setTotalGain(String totalGain) {
		this.totalGain = totalGain;
	}




	/**
	 * @return the userGain
	 */
	public String getUserGain() {
		return userGain;
	}




	/**
	 * @param userGain the userGain to set
	 */
	public void setUserGain(String userGain) {
		this.userGain = userGain;
	}




	/**
	 * @return the tradingCost
	 */
	public String getTradingCost() {
		return tradingCost;
	}




	/**
	 * @param tradingCost the tradingCost to set
	 */
	public void setTradingCost(String tradingCost) {
		this.tradingCost = tradingCost;
	}




	/**
	 * @return the accountPay
	 */
	public String getAccountPay() {
		return accountPay;
	}




	/**
	 * @param accountPay the accountPay to set
	 */
	public void setAccountPay(String accountPay) {
		this.accountPay = accountPay;
	}




	/**
	 * @return the capitalRate
	 */
	public String getCapitalRate() {
		return capitalRate;
	}




	/**
	 * @param capitalRate the capitalRate to set
	 */
	public void setCapitalRate(String capitalRate) {
		this.capitalRate = capitalRate;
	}




	/**
	 * @return the frozenAmount
	 */
	public String getFrozenAmount() {
		return frozenAmount;
	}




	/**
	 * @param frozenAmount the frozenAmount to set
	 */
	public void setFrozenAmount(String frozenAmount) {
		this.frozenAmount = frozenAmount;
	}




	/**
	 * @return the payOut
	 */
	public String getPayOut() {
		return payOut;
	}




	/**
	 * @param payOut the payOut to set
	 */
	public void setPayOut(String payOut) {
		this.payOut = payOut;
	}




	/**
	 * @return the unfreezeAmount
	 */
	public String getUnfreezeAmount() {
		return unfreezeAmount;
	}




	/**
	 * @param unfreezeAmount the unfreezeAmount to set
	 */
	public void setUnfreezeAmount(String unfreezeAmount) {
		this.unfreezeAmount = unfreezeAmount;
	}




	/**
	 * @return the buyAverage
	 */
	public String getBuyAverage() {
		return buyAverage;
	}




	/**
	 * @param buyAverage the buyAverage to set
	 */
	public void setBuyAverage(String buyAverage) {
		this.buyAverage = buyAverage;
	}




	/**
	 * @return the sellAverage
	 */
	public String getSellAverage() {
		return sellAverage;
	}




	/**
	 * @param sellAverage the sellAverage to set
	 */
	public void setSellAverage(String sellAverage) {
		this.sellAverage = sellAverage;
	}




	/**
	 * @return the tradingDate
	 */
	public String getTradingDate() {
		return tradingDate;
	}




	/**
	 * @param tradingDate the tradingDate to set
	 */
	public void setTradingDate(String tradingDate) {
		this.tradingDate = tradingDate;
	}




	/**
	 * @return the virtual
	 */
	public boolean isVirtual() {
		return virtual;
	}




	/**
	 * @param virtual the virtual to set
	 */
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}




	/**
	 * @return the buyDay
	 */
	public String getBuyDay() {
		return buyDay;
	}




	/**
	 * @param buyDay the buyDay to set
	 */
	public void setBuyDay(String buyDay) {
		this.buyDay = buyDay;
	}




	/**
	 * @return the deadline
	 */
	public String getDeadline() {
		return deadline;
	}




	/**
	 * @param deadline the deadline to set
	 */
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}




	/**
	 * @return the cost
	 */
	public String getCost() {
		return cost;
	}




	/**
	 * @param cost the cost to set
	 */
	public void setCost(String cost) {
		this.cost = cost;
	}




	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}




	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}




	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}




	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}




	@Override
	public String toString() {
		return "AuditDetailVO [accountPay=" + accountPay + ", id=" + id
				+ ", buyAverage=" + buyAverage + ", buyDay=" + buyDay
				+ ", capitalRate=" + capitalRate + ", cost=" + cost
				+ ", deadline=" + deadline + ", frozenAmount=" + frozenAmount
				+ ", fund=" + fund + ", payOut=" + payOut + ", plRisk="
				+ plRisk + ", sellAverage=" + sellAverage + ", totalGain="
				+ totalGain + ", tradingCost=" + tradingCost + ", tradingDate="
				+ tradingDate + ", unfreezeAmount=" + unfreezeAmount
				+ ", userGain=" + userGain + ", virtual=" + virtual +", type="+type+ "]";
	}
}
