package com.wxxr.stock.trading.ejb.api;


import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UserCreateTradAccInfoVO")
public class UserCreateTradAccInfoVO implements Serializable{
	private static final long serialVersionUID = 6046188597351046383L;
	@XmlElement(name = "depositRate")
	private float depositRate;//保证金比例
	@XmlElement(name="costRate")
	private float costRate;//综合费用比例,手续费
	@XmlElement(name="capitalRate")
	private float capitalRate;//止损比例
	@XmlElement(name="maxAmount")
	private Long maxAmount;//可申请最大金额
	@XmlElement(name="userId")
	private String userId;//用户唯一标识
	@XmlElement(name="rateString")
	private String rateString;//止损+保证金
	@XmlElement(name="voucherCostRate")
	private float voucherCostRate;//实盘券综合费用比例,手续费
	
	
	public float getDepositRate() {
		return depositRate;
	}
	public void setDepositRate(float depositRate) {
		this.depositRate = depositRate;
	}
	
	public float getCostRate() {
		return costRate;
	}
	public void setCostRate(float costRate) {
		this.costRate = costRate;
	}
	
	public float getCapitalRate() {
		return capitalRate;
	}
	public void setCapitalRate(float capitalRate) {
		this.capitalRate = capitalRate;
	}
	
	public Long getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(Long maxAmount) {
		this.maxAmount = maxAmount;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getRateString() {
		return rateString;
	}
	public void setRateString(String rateString) {
		this.rateString = rateString;
	}
	
	/**
	 * @return the voucherCostRate
	 */
	
	public float getVoucherCostRate() {
		return voucherCostRate;
	}
	/**
	 * @param voucherCostRate the voucherCostRate to set
	 */
	public void setVoucherCostRate(float voucherCostRate) {
		this.voucherCostRate = voucherCostRate;
	}
	
	@Override
	public String toString() {
		return "UserCreateTradAccInfoVO [costRate=" + costRate + ", maxAmount="
				+ maxAmount + ", rateString=" + rateString + ", userId="
				+ userId + ", voucherCostRate="+voucherCostRate+"]";
	}
	
	
	
	
}
