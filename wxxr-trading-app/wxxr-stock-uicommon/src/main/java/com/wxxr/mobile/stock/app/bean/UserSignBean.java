/**
 * 
 */
package com.wxxr.mobile.stock.app.bean;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;


public class UserSignBean implements IBindableBean {
	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	private Boolean sign = false;
	private long ongoingDays;
	private long rewardVol;
	private String signDate;
	private Integer success;//失败为-1，重复签到为：0，成功为：1；
	private String failReason;
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
	
	@Override
	public boolean hasPropertyChangeListener(IPropertyChangeListener listener) {
		return this.emitter.hasPropertyChangeListener(listener);
	}	


	public Boolean getSign() {
		return sign;
	}

	public void setSign(Boolean sign) {
		Boolean old = this.sign;
		this.sign = sign;
		this.emitter.firePropertyChange("sign", old, this.sign);
	}

	public long getOngoingDays() {
		return ongoingDays;
	}

	public void setOngoingDays(long ongoingDays) {
		long old = this.ongoingDays;
		this.ongoingDays = ongoingDays;
		this.emitter.firePropertyChange("ongoingDays", old, this.ongoingDays);
		
	}

	public long getRewardVol() {
		return rewardVol;
	}

	public void setRewardVol(long rewardVol) {
		long old = this.rewardVol;
		this.rewardVol = rewardVol;
		this.emitter.firePropertyChange("rewardVol", old, this.rewardVol);
		
	}

	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		String old = this.signDate;
		this.signDate = signDate;
		this.emitter.firePropertyChange("signDate", old, this.signDate);
		
	}

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		Integer old = this.success;
		this.success = success;
		this.emitter.firePropertyChange("success", old, this.success);
		
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		String old = this.failReason;
		this.failReason = failReason;
		this.emitter.firePropertyChange("failReason", old, this.failReason);
		
	}

	@Override
	public String toString() {
		return "UserSignBean [sign=" + sign + ", ongoingDays=" + ongoingDays
				+ ", rewardVol=" + rewardVol + ", signDate=" + signDate
				+ ", success=" + success + ", failReason=" + failReason + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((failReason == null) ? 0 : failReason.hashCode());
		result = prime * result + (int) (ongoingDays ^ (ongoingDays >>> 32));
		result = prime * result + (int) (rewardVol ^ (rewardVol >>> 32));
		result = prime * result + ((sign == null) ? 0 : sign.hashCode());
		result = prime * result
				+ ((signDate == null) ? 0 : signDate.hashCode());
		result = prime * result + ((success == null) ? 0 : success.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSignBean other = (UserSignBean) obj;
		if (failReason == null) {
			if (other.failReason != null)
				return false;
		} else if (!failReason.equals(other.failReason))
			return false;
		if (ongoingDays != other.ongoingDays)
			return false;
		if (rewardVol != other.rewardVol)
			return false;
		if (sign == null) {
			if (other.sign != null)
				return false;
		} else if (!sign.equals(other.sign))
			return false;
		if (signDate == null) {
			if (other.signDate != null)
				return false;
		} else if (!signDate.equals(other.signDate))
			return false;
		if (success == null) {
			if (other.success != null)
				return false;
		} else if (!success.equals(other.success))
			return false;
		return true;
	}


}
