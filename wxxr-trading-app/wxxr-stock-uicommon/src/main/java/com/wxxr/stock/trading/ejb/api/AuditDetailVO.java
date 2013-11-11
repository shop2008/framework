package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
/**
 * 
 * @author chenchao
 *
 */
@XmlRootElement(name = "AuditDetailVO")
public class AuditDetailVO implements Serializable{
	
	private static final long serialVersionUID = -100031906976257734L;

	/**申请资金*/
	@XmlElement(name = "fund")
	private String fund;
	/**总盈亏率*/
	@XmlElement(name = "plRisk")
	private String plRisk;
	/**盈亏总额（交易盘，除去费用）*/
	@XmlElement(name = "totalGain")
	private String totalGain;
	/**玩家实得收益--没有收益时不显示80%*/
	@XmlElement(name = "userGain")
	private String userGain;
	/**补偿交易综合费--总收益<手续费，=总收益（优先补偿)*/
	@XmlElement(name = "tradingCost")
	private String tradingCost;
	/**账户管理费--没有收益时不显示20%*/
	@XmlElement(name = "accountPay")
	private String accountPay;
	/**止损比例*/
	@XmlElement(name = "capitalRate")
	private String capitalRate;
	/**冻结资金*/
	@XmlElement(name = "frozenAmount")
	private String frozenAmount;
	/**扣减数*/
	@XmlElement(name = "payOut")
	private String payOut;
	/**解冻数量*/
	@XmlElement(name = "unfreezeAmount")
	private String unfreezeAmount;
	/**买入均价*/
	@XmlElement(name = "buyAverage")
	private String buyAverage;
	/**卖出均价*/
	@XmlElement(name = "sellAverage")
	private String sellAverage;
	/**交易结算日期*/
	@XmlElement(name = "tradingDate")
	private String tradingDate;
	/**交易盘类型*/
	@XmlElement(name = "virtual")
	private boolean virtual;
	/**申请交易盘时间*/
	@XmlElement(name = "buyDay")
	private String buyDay;
	/**存续时间*/
	@XmlElement(name = "deadline")
	private String deadline;
	/**手续费*/
	@XmlElement(name = "cost")
	private String cost;
	/**交易盘编号*/
	@XmlElement(name = "id")
	private String id;
	/**交易盘类型*/
	@XmlElement(name = "type")
	private String type;
	
	
	public String getFund() {
		return fund;
	}
	public void setFund(String fund) {
		this.fund = fund;
	}
	
	public String getPlRisk() {
		return plRisk;
	}
	public void setPlRisk(String plRisk) {
		this.plRisk = plRisk;
	}
	
	public String getTotalGain() {
		return totalGain;
	}
	public void setTotalGain(String totalGain) {
		this.totalGain = totalGain;
	}
	
	public String getUserGain() {
		return userGain;
	}
	public void setUserGain(String userGain) {
		this.userGain = userGain;
	}
	
	public String getTradingCost() {
		return tradingCost;
	}
	public void setTradingCost(String tradingCost) {
		this.tradingCost = tradingCost;
	}
	
	public String getAccountPay() {
		return accountPay;
	}
	public void setAccountPay(String accountPay) {
		this.accountPay = accountPay;
	}
	
	public String getCapitalRate() {
		return capitalRate;
	}
	public void setCapitalRate(String capitalRate) {
		this.capitalRate = capitalRate;
	}
	
	public String getFrozenAmount() {
		return frozenAmount;
	}
	public void setFrozenAmount(String frozenAmount) {
		this.frozenAmount = frozenAmount;
	}
	
	public String getPayOut() {
		return payOut;
	}
	public void setPayOut(String payOut) {
		this.payOut = payOut;
	}
	
	public String getUnfreezeAmount() {
		return unfreezeAmount;
	}
	public void setUnfreezeAmount(String unfreezeAmount) {
		this.unfreezeAmount = unfreezeAmount;
	}
	
	public String getBuyAverage() {
		return buyAverage;
	}
	public void setBuyAverage(String buyAverage) {
		this.buyAverage = buyAverage;
	}
	
	public String getSellAverage() {
		return sellAverage;
	}
	public void setSellAverage(String sellAverage) {
		this.sellAverage = sellAverage;
	}
	
	public String getTradingDate() {
		return tradingDate;
	}
	public void setTradingDate(String tradingDate) {
		this.tradingDate = tradingDate;
	}
	
	public boolean isVirtual() {
		return virtual;
	}
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}
	
	public String getBuyDay() {
		return buyDay;
	}
	public void setBuyDay(String buyDay) {
		this.buyDay = buyDay;
	}
	
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
