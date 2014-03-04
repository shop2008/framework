package com.wxxr.mobile.stock.app.bean;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;

public class GuideGainBean implements IBindableBean{

	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	private String guideGain ="0";

	private boolean allow = true;

	public String getGuideGain() {
		return guideGain;
	}

	public void setGuideGain(String guideGain) {
		String old = this.guideGain;
		this.guideGain = guideGain;
		this.emitter.firePropertyChange("guideGain", old, this.guideGain);
	}

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


	public boolean getAllow() {
		return allow;
	}

	public void setAllow(boolean allow) {
		boolean old = this.allow;
		this.allow = allow;
		this.emitter.firePropertyChange("allow", old, this.allow);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (allow ? 1231 : 1237);
		result = prime * result
				+ ((guideGain == null) ? 0 : guideGain.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GuideGainBean other = (GuideGainBean) obj;
		if (allow != other.allow)
			return false;
		if (guideGain == null) {
			if (other.guideGain != null)
				return false;
		} else if (!guideGain.equals(other.guideGain))
			return false;
		return true;
	}



}
