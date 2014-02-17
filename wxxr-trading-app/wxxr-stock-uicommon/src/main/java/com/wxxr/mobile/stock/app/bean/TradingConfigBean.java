/**
 * 
 */
package com.wxxr.mobile.stock.app.bean;

import java.util.List;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;
import com.wxxr.mobile.core.ui.common.ListDecorator;
import com.wxxr.stock.trading.ejb.api.LossRateNDepositRate;
import com.wxxr.stock.trading.ejb.api.TradingExtendConfig;

/**
 * @author wangxuyang
 *
 */
public class TradingConfigBean implements IBindableBean{

	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
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
	private List<Long> accualOptions;
	private List<Long> virtualOptions;
	private List<Long> voucherOptions;
	private String delayFee;//递延费
	private List<TradingExtendConfig> extendConfig;//预留扩展属性
	/**
	 * @param listener
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		emitter.addPropertyChangeListener(listener);
	}

	/**
	 * @param listener
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		emitter.removePropertyChangeListener(listener);
	}

	public String getVoIdentity() {
		return voIdentity;
	}

	public void setVoIdentity(String voIdentity) {
		String old = this.voIdentity;
		this.voIdentity = voIdentity;
		this.emitter.firePropertyChange("voIdentity", old, voIdentity);
	}

	public String getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(String applyAmount) {
		String old = this.applyAmount;
		this.applyAmount = applyAmount;
		this.emitter.firePropertyChange("applyAmount", old, applyAmount);
	}

	public String getCreateStartDate() {
		return createStartDate;
	}

	public void setCreateStartDate(String createStartDate) {
		String old = this.createStartDate;
		this.createStartDate = createStartDate;
		this.emitter.firePropertyChange("createStartDate", old, createStartDate);
	}

	public String getCreateEndDate() {
		return createEndDate;
	}

	public void setCreateEndDate(String createEndDate) {
		String old = this.createEndDate;
		this.createEndDate = createEndDate;
		this.emitter.firePropertyChange("createEndDate", old, createEndDate);
	}

	public String getClearDate() {
		return clearDate;
	}

	public void setClearDate(String clearDate) {
		String old = this.clearDate;
		this.clearDate = clearDate;
		this.emitter.firePropertyChange("clearDate", old, clearDate);
	}

	public String getMaxBuyAmount() {
		return maxBuyAmount;
	}

	public void setMaxBuyAmount(String maxBuyAmount) {
		String old = this.maxBuyAmount;
		this.maxBuyAmount = maxBuyAmount;
		this.emitter.firePropertyChange("maxBuyAmount", old, maxBuyAmount);
	}

	public String getOriginalFee() {
		return originalFee;
	}

	public void setOriginalFee(String originalFee) {
		String old = this.originalFee;
		this.originalFee = originalFee;
		this.emitter.firePropertyChange("originalFee", old, originalFee);
	}

	public String getDiscountFee() {
		return discountFee;
	}

	public void setDiscountFee(String discountFee) {
		String old = this.discountFee;
		this.discountFee = discountFee;
		this.emitter.firePropertyChange("discountFee", old, discountFee);
	}

	public String getCompanyGainRate() {
		return companyGainRate;
	}

	public void setCompanyGainRate(String companyGainRate) {
		String old = this.companyGainRate;
		this.companyGainRate = companyGainRate;
		this.emitter.firePropertyChange("companyGainRate", old, companyGainRate);
	}

	public List<LossRateNDepositRate> getRateList() {
		return rateList;
	}

	public void setRateList(List<LossRateNDepositRate> rateList) {
		List<LossRateNDepositRate> old = this.rateList;
		this.rateList = rateList;
		if(this.rateList != null){
            this.rateList = new ListDecorator<LossRateNDepositRate>("rateList", this.emitter,this.rateList);
        }
		this.emitter.firePropertyChange("rateList", old, this.rateList);
	}

	public List<LossRateNDepositRate> getVirtualRateList() {
		return virtualRateList;
	}

	public void setVirtualRateList(List<LossRateNDepositRate> virtualRateList) {
		List<LossRateNDepositRate> old = this.virtualRateList;
		this.virtualRateList = virtualRateList;
		if(this.virtualRateList != null){
            this.virtualRateList = new ListDecorator<LossRateNDepositRate>("virtualRateList", this.emitter,this.virtualRateList);
        }
		this.emitter.firePropertyChange("virtualRateList", old, this.virtualRateList);
	}

	public List<LossRateNDepositRate> getVoucherRateList() {
		return voucherRateList;
	}

	public void setVoucherRateList(List<LossRateNDepositRate> voucherRateList) {
		List<LossRateNDepositRate> old = this.voucherRateList;
		this.voucherRateList = voucherRateList;
		if(this.voucherRateList != null){
            this.voucherRateList = new ListDecorator<LossRateNDepositRate>("voucherRateList", this.emitter,this.voucherRateList);
        }
		this.emitter.firePropertyChange("voucherRateList", old, this.voucherRateList);
	}

	public String getVoucherApplyAmount() {
		return voucherApplyAmount;
	}

	public void setVoucherApplyAmount(String voucherApplyAmount) {
		String old = this.voucherApplyAmount;
		this.voucherApplyAmount = voucherApplyAmount;
		this.emitter.firePropertyChange("voucherApplyAmount", old, voucherApplyAmount);
	}

	public String getVirtualApplyAmount() {
		return virtualApplyAmount;
	}

	public void setVirtualApplyAmount(String virtualApplyAmount) {
		String old = this.virtualApplyAmount;
		this.virtualApplyAmount = virtualApplyAmount;
		this.emitter.firePropertyChange("virtualApplyAmount", old, virtualApplyAmount);
	}

	public List<Long> getAccualOptions() {
		return accualOptions;
	}

	public void setAccualOptions(List<Long> accualOptions) {
		List<Long> old = this.accualOptions;
		this.accualOptions = accualOptions;
		if(this.accualOptions != null){
            this.accualOptions = new ListDecorator<Long>("accualOptions", this.emitter,this.accualOptions);
        }
		this.emitter.firePropertyChange("accualOptions", old, this.accualOptions);
		
	}

	public List<Long> getVirtualOptions() {
		return virtualOptions;
	}

	public void setVirtualOptions(List<Long> virtualOptions) {
		List<Long> old = this.virtualOptions;
		this.virtualOptions = virtualOptions;
		if(this.virtualOptions != null){
            this.virtualOptions = new ListDecorator<Long>("virtualOptions", this.emitter,this.virtualOptions);
        }
		this.emitter.firePropertyChange("virtualOptions", old, this.virtualOptions);
	}

	public List<Long> getVoucherOptions() {
		return voucherOptions;
	}

	public void setVoucherOptions(List<Long> voucherOptions) {
		List<Long> old = this.voucherOptions;
		this.voucherOptions = voucherOptions;
		if(this.voucherOptions != null){
            this.voucherOptions = new ListDecorator<Long>("voucherOptions", this.emitter,this.voucherOptions);
        }
		this.emitter.firePropertyChange("voucherOptions", old, this.voucherOptions);
	}

	public List<TradingExtendConfig> getExtendConfig() {
		return extendConfig;
	}

	public void setExtendConfig(List<TradingExtendConfig> extendConfig) {
		List<TradingExtendConfig> old = this.extendConfig;
		this.extendConfig = extendConfig;
		if(this.extendConfig != null){
            this.extendConfig = new ListDecorator<TradingExtendConfig>("extendConfig", this.emitter,this.extendConfig);
        }
		this.emitter.firePropertyChange("extendConfig", old, this.extendConfig);
	}

	public String getDelayFee() {
		return delayFee;
	}

	public void setDelayFee(String delayFee) {
		String old = this.delayFee;
		this.delayFee = delayFee;
		this.emitter.firePropertyChange("delayFee", old, delayFee);
	}

	

	@Override
	public String toString() {
		return "TradingConfigBean [voIdentity=" + voIdentity + ", applyAmount="
				+ applyAmount + ", createStartDate=" + createStartDate
				+ ", createEndDate=" + createEndDate + ", clearDate="
				+ clearDate + ", maxBuyAmount=" + maxBuyAmount
				+ ", originalFee=" + originalFee + ", discountFee="
				+ discountFee + ", companyGainRate=" + companyGainRate
				+ ", rateList=" + rateList + ", virtualRateList="
				+ virtualRateList + ", voucherRateList=" + voucherRateList
				+ ", voucherApplyAmount=" + voucherApplyAmount
				+ ", virtualApplyAmount=" + virtualApplyAmount
				+ ", accualOptions=" + accualOptions + ", virtualOptions="
				+ virtualOptions + ", voucherOptions=" + voucherOptions
				+ ", delayFee=" + delayFee + ", extendConfig=" + extendConfig
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accualOptions == null) ? 0 : accualOptions.hashCode());
		result = prime * result
				+ ((applyAmount == null) ? 0 : applyAmount.hashCode());
		result = prime * result
				+ ((clearDate == null) ? 0 : clearDate.hashCode());
		result = prime * result
				+ ((companyGainRate == null) ? 0 : companyGainRate.hashCode());
		result = prime * result
				+ ((createEndDate == null) ? 0 : createEndDate.hashCode());
		result = prime * result
				+ ((createStartDate == null) ? 0 : createStartDate.hashCode());
		result = prime * result
				+ ((delayFee == null) ? 0 : delayFee.hashCode());
		result = prime * result
				+ ((discountFee == null) ? 0 : discountFee.hashCode());
		result = prime * result
				+ ((extendConfig == null) ? 0 : extendConfig.hashCode());
		result = prime * result
				+ ((maxBuyAmount == null) ? 0 : maxBuyAmount.hashCode());
		result = prime * result
				+ ((originalFee == null) ? 0 : originalFee.hashCode());
		result = prime * result
				+ ((rateList == null) ? 0 : rateList.hashCode());
		result = prime
				* result
				+ ((virtualApplyAmount == null) ? 0 : virtualApplyAmount
						.hashCode());
		result = prime * result
				+ ((virtualOptions == null) ? 0 : virtualOptions.hashCode());
		result = prime * result
				+ ((virtualRateList == null) ? 0 : virtualRateList.hashCode());
		result = prime * result
				+ ((voIdentity == null) ? 0 : voIdentity.hashCode());
		result = prime
				* result
				+ ((voucherApplyAmount == null) ? 0 : voucherApplyAmount
						.hashCode());
		result = prime * result
				+ ((voucherOptions == null) ? 0 : voucherOptions.hashCode());
		result = prime * result
				+ ((voucherRateList == null) ? 0 : voucherRateList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TradingConfigBean other = (TradingConfigBean) obj;
		if (accualOptions == null) {
			if (other.accualOptions != null)
				return false;
		} else if (!accualOptions.equals(other.accualOptions))
			return false;
		if (applyAmount == null) {
			if (other.applyAmount != null)
				return false;
		} else if (!applyAmount.equals(other.applyAmount))
			return false;
		if (clearDate == null) {
			if (other.clearDate != null)
				return false;
		} else if (!clearDate.equals(other.clearDate))
			return false;
		if (companyGainRate == null) {
			if (other.companyGainRate != null)
				return false;
		} else if (!companyGainRate.equals(other.companyGainRate))
			return false;
		if (createEndDate == null) {
			if (other.createEndDate != null)
				return false;
		} else if (!createEndDate.equals(other.createEndDate))
			return false;
		if (createStartDate == null) {
			if (other.createStartDate != null)
				return false;
		} else if (!createStartDate.equals(other.createStartDate))
			return false;
		if (delayFee == null) {
			if (other.delayFee != null)
				return false;
		} else if (!delayFee.equals(other.delayFee))
			return false;
		if (discountFee == null) {
			if (other.discountFee != null)
				return false;
		} else if (!discountFee.equals(other.discountFee))
			return false;
		if (extendConfig == null) {
			if (other.extendConfig != null)
				return false;
		} else if (!extendConfig.equals(other.extendConfig))
			return false;
		if (maxBuyAmount == null) {
			if (other.maxBuyAmount != null)
				return false;
		} else if (!maxBuyAmount.equals(other.maxBuyAmount))
			return false;
		if (originalFee == null) {
			if (other.originalFee != null)
				return false;
		} else if (!originalFee.equals(other.originalFee))
			return false;
		if (rateList == null) {
			if (other.rateList != null)
				return false;
		} else if (!rateList.equals(other.rateList))
			return false;
		if (virtualApplyAmount == null) {
			if (other.virtualApplyAmount != null)
				return false;
		} else if (!virtualApplyAmount.equals(other.virtualApplyAmount))
			return false;
		if (virtualOptions == null) {
			if (other.virtualOptions != null)
				return false;
		} else if (!virtualOptions.equals(other.virtualOptions))
			return false;
		if (virtualRateList == null) {
			if (other.virtualRateList != null)
				return false;
		} else if (!virtualRateList.equals(other.virtualRateList))
			return false;
		if (voIdentity == null) {
			if (other.voIdentity != null)
				return false;
		} else if (!voIdentity.equals(other.voIdentity))
			return false;
		if (voucherApplyAmount == null) {
			if (other.voucherApplyAmount != null)
				return false;
		} else if (!voucherApplyAmount.equals(other.voucherApplyAmount))
			return false;
		if (voucherOptions == null) {
			if (other.voucherOptions != null)
				return false;
		} else if (!voucherOptions.equals(other.voucherOptions))
			return false;
		if (voucherRateList == null) {
			if (other.voucherRateList != null)
				return false;
		} else if (!voucherRateList.equals(other.voucherRateList))
			return false;
		return true;
	}

	
	

}
