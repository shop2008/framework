package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;
import java.util.List;




public class TradingConfigVO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String voIdentity;//VO标识
	private String applyAmount;//申购金额
	private String createStartDate;//创建开始时间
	private String createEndDate;//创建结束时间
	private String clearDate;//清盘时间
	private String maxBuyAmount;//最大买入标的只数
	private String originalFee;//原价-每万元手续费
	private String discountFee;//优惠价
	private String companyGainRate;//公司分成比率
	private List<LossRateNDepositRate> rateList;//止损和保证金额度
	private List<LossRateNDepositRate> virtualRateList;//虚拟盘止损率和保证金
	private List<LossRateNDepositRate> voucherRateList;//积分盘止损率和保证金
	private String voucherApplyAmount;//积分盘申购金额
	private String virtualApplyAmount;//虚拟盘申购额度
	private String delayFee;//递延费
	private List<TradingExtendConfig> extendConfig;//预留扩展属性
	
	
	/**
	 * @return the voIdentity
	 */

	public String getVoIdentity() {
		return voIdentity;
	}
	/**
	 * @param voIdentity the voIdentity to set
	 */
	public void setVoIdentity(String voIdentity) {
		this.voIdentity = voIdentity;
	}
	/**
	 * @return the virtualRateList
	 */
	public List<LossRateNDepositRate> getVirtualRateList() {
		return virtualRateList;
	}
	/**
	 * @param virtualRateList the virtualRateList to set
	 */
	public void setVirtualRateList(List<LossRateNDepositRate> virtualRateList) {
		this.virtualRateList = virtualRateList;
	}
	/**
	 * @return the voucherApplyAmount
	 */
	public String getVoucherApplyAmount() {
		return voucherApplyAmount;
	}
	/**
	 * @param voucherApplyAmount the voucherApplyAmount to set
	 */
	public void setVoucherApplyAmount(String voucherApplyAmount) {
		this.voucherApplyAmount = voucherApplyAmount;
	}
	/**
	 * @return the virtualApplyAmount
	 */
	public String getVirtualApplyAmount() {
		return virtualApplyAmount;
	}
	/**
	 * @param virtualApplyAmount the virtualApplyAmount to set
	 */
	public void setVirtualApplyAmount(String virtualApplyAmount) {
		this.virtualApplyAmount = virtualApplyAmount;
	}
	
	public List<LossRateNDepositRate> getVoucherRateList() {
		return voucherRateList;
	}
	/**
	 * @param voucherRateList the voucherRateList to set
	 */
	public void setVoucherRateList(List<LossRateNDepositRate> voucherRateList) {
		this.voucherRateList = voucherRateList;
	}
	/**
	 * @return the applyAmount
	 */
	public String getApplyAmount() {
		return applyAmount;
	}
	/**
	 * @param applyAmount the applyAmount to set
	 */
	public void setApplyAmount(String applyAmount) {
		this.applyAmount = applyAmount;
	}
	/**
	 * @return the createStartDate
	 */
	public String getCreateStartDate() {
		return createStartDate;
	}
	/**
	 * @param createStartDate the createStartDate to set
	 */
	public void setCreateStartDate(String createStartDate) {
		this.createStartDate = createStartDate;
	}
	/**
	 * @return the createEndDate
	 */
	public String getCreateEndDate() {
		return createEndDate;
	}
	/**
	 * @param createEndDate the createEndDate to set
	 */
	public void setCreateEndDate(String createEndDate) {
		this.createEndDate = createEndDate;
	}
	/**
	 * @return the clearDate
	 */
	public String getClearDate() {
		return clearDate;
	}
	/**
	 * @param clearDate the clearDate to set
	 */
	public void setClearDate(String clearDate) {
		this.clearDate = clearDate;
	}
	/**
	 * @return the maxBuyAmount
	 */
	public String getMaxBuyAmount() {
		return maxBuyAmount;
	}
	/**
	 * @param maxBuyAmount the maxBuyAmount to set
	 */
	public void setMaxBuyAmount(String maxBuyAmount) {
		this.maxBuyAmount = maxBuyAmount;
	}
	/**
	 * @return the originalFee
	 */
	public String getOriginalFee() {
		return originalFee;
	}
	/**
	 * @param originalFee the originalFee to set
	 */
	public void setOriginalFee(String originalFee) {
		this.originalFee = originalFee;
	}
	/**
	 * @return the discountFee
	 */
	public String getDiscountFee() {
		return discountFee;
	}
	/**
	 * @param discountFee the discountFee to set
	 */
	public void setDiscountFee(String discountFee) {
		this.discountFee = discountFee;
	}
	/**
	 * @return the companyGainRate
	 */
	public String getCompanyGainRate() {
		return companyGainRate;
	}
	/**
	 * @param companyGainRate the companyGainRate to set
	 */
	public void setCompanyGainRate(String companyGainRate) {
		this.companyGainRate = companyGainRate;
	}
	/**
	 * @return the rateList
	 */
	public List<LossRateNDepositRate> getRateList() {
		return rateList;
	}
	/**
	 * @param rateList the rateList to set
	 */
	public void setRateList(List<LossRateNDepositRate> rateList) {
		this.rateList = rateList;
	}
	
	/**
	 * @return the extendConfig
	 */
	public List<TradingExtendConfig> getExtendConfig() {
		return extendConfig;
	}
	/**
	 * @param extendConfig the extendConfig to set
	 */
	public void setExtendConfig(List<TradingExtendConfig> extendConfig) {
		this.extendConfig = extendConfig;
	}
	
	public String getDelayFee() {
		return delayFee;
	}
	public void setDelayFee(String delayFee) {
		this.delayFee = delayFee;
	}
	@Override
	public String toString() {
		return "TradingConfigVO [voIdentity=" + voIdentity + ", applyAmount="
				+ applyAmount + ", createStartDate=" + createStartDate
				+ ", createEndDate=" + createEndDate + ", clearDate="
				+ clearDate + ", maxBuyAmount=" + maxBuyAmount
				+ ", originalFee=" + originalFee + ", discountFee="
				+ discountFee + ", companyGainRate=" + companyGainRate
				+ ", rateList=" + rateList + ", virtualRateList="
				+ virtualRateList + ", voucherRateList=" + voucherRateList
				+ ", voucherApplyAmount=" + voucherApplyAmount
				+ ", virtualApplyAmount=" + virtualApplyAmount + ", delayFee="
				+ delayFee + ", extendConfig=" + extendConfig + "]";
	}
	
	
}
