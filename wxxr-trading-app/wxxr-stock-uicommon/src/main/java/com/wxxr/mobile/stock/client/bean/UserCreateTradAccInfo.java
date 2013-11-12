/**
 * 
 */
package com.wxxr.mobile.stock.client.bean;

import com.wxxr.mobile.core.annotation.BindableBean;


/**
 * @author wangxuyang
 *
 */
@BindableBean
public class UserCreateTradAccInfo {
	/**
	 * 保证金比例
	 */
	private float depositRate;
	/**
	 * 综合费用比例,手续费
	 */
	private float costRate;
	/**
	 * 止损比例
	 */
	private float capitalRate;
	/**
	 * 可申请最大金额
	 */
	private Long maxAmount;
	/**
	 * 用户唯一标识
	 */
	private String userId;
	/**
	 * 止损+保证金
	 */
	private String rateString;
	/**
	 * 实盘券综合费用比例,手续费
	 */
	private float voucherCostRate;
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
	public float getVoucherCostRate() {
		return voucherCostRate;
	}
	public void setVoucherCostRate(float voucherCostRate) {
		this.voucherCostRate = voucherCostRate;
	}
	
	
	
}
