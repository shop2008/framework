package com.wxxr.stock.trading.ejb.api;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
@XmlRootElement(name = "UserCreateTradAccInfoVO")
public class UserCreateTradAccInfoVO implements Serializable{
	private static final long serialVersionUID = 6046188597351046383L;
	private float depositRate;//保证金比例
	private float costRate;//综合费用比例,手续费
	private float costRateV;//模拟综合费用比例,手续费
	private float costRateT;//t综合费用比例,手续费
	private float costRateT3;//t3综合费用比例,手续费
	private float costRateTN;//t+n综合费用比例,手续费
	private float capitalRate;//止损比例
	private Long maxAmount;//可申请最大金额
	private String userId;//用户唯一标识
	private String rateString;//止损+保证金
	private String rateStringT;//t止损+保证金
	private String rateStringT3;//t3止损+保证金
	private String rateStringTN;//t+n止损+保证金
	private String rateStringV;//模拟止损+保证金
	private float voucherCostRate;//实盘券综合费用比例,手续费
	public float getCostRateV() {
		return costRateV;
	}
	public void setCostRateV(float costRateV) {
		this.costRateV = costRateV;
	}
	public String getRateStringT() {
		return rateStringT;
	}
	public void setRateStringT(String rateStringT) {
		this.rateStringT = rateStringT;
	}
	public String getRateStringT3() {
		return rateStringT3;
	}
	public void setRateStringT3(String rateStringT3) {
		this.rateStringT3 = rateStringT3;
	}
	public String getRateStringV() {
		return rateStringV;
	}
	public void setRateStringV(String rateStringV) {
		this.rateStringV = rateStringV;
	}
	@XmlElement
	public float getDepositRate() {
		return depositRate;
	}
	public void setDepositRate(float depositRate) {
		this.depositRate = depositRate;
	}
	@XmlElement
	public float getCostRate() {
		return costRate;
	}
	public void setCostRate(float costRate) {
		this.costRate = costRate;
	}
	@XmlElement
	public float getCapitalRate() {
		return capitalRate;
	}
	public void setCapitalRate(float capitalRate) {
		this.capitalRate = capitalRate;
	}
	@XmlElement
	public Long getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(Long maxAmount) {
		this.maxAmount = maxAmount;
	}
	@XmlElement
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@XmlElement
	public String getRateString() {
		return rateString;
	}
	public void setRateString(String rateString) {
		this.rateString = rateString;
	}
	
	
	
	public float getCostRateTN() {
		return costRateTN;
	}
	public void setCostRateTN(float costRateTN) {
		this.costRateTN = costRateTN;
	}
	public String getRateStringTN() {
		return rateStringTN;
	}
	public void setRateStringTN(String rateStringTN) {
		this.rateStringTN = rateStringTN;
	}
	/**
	 * @return the voucherCostRate
	 */
	@XmlElement
	public float getVoucherCostRate() {
		return voucherCostRate;
	}
	/**
	 * @param voucherCostRate the voucherCostRate to set
	 */
	public void setVoucherCostRate(float voucherCostRate) {
		this.voucherCostRate = voucherCostRate;
	}
	
	public float getCostRateT() {
		return costRateT;
	}
	public void setCostRateT(float costRateT) {
		this.costRateT = costRateT;
	}
	public float getCostRateT3() {
		return costRateT3;
	}
	public void setCostRateT3(float costRateT3) {
		this.costRateT3 = costRateT3;
	}
	@Override
	public String toString() {
		return "UserCreateTradAccInfoVO [depositRate=" + depositRate
				+ ", costRate=" + costRate + ", costRateV=" + costRateV
				+ ", costRateT=" + costRateT + ", costRateT3=" + costRateT3
				+ ", costRateTN=" + costRateTN + ", capitalRate=" + capitalRate
				+ ", maxAmount=" + maxAmount + ", userId=" + userId
				+ ", rateString=" + rateString + ", rateStringT=" + rateStringT
				+ ", rateStringT3=" + rateStringT3 + ", rateStringTN="
				+ rateStringTN + ", rateStringV=" + rateStringV
				+ ", voucherCostRate=" + voucherCostRate + "]";
	}
	
	
	
	
}
