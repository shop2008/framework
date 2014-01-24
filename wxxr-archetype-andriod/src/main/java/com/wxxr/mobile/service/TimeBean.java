/**
 * 
 */
package com.wxxr.mobile.service;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;

/**
 * @author neillin
 *
 */
public class TimeBean implements IBindableBean{

	private BindableBeanSupport support = new BindableBeanSupport(this);

	/**
	 * @param listener
	 * @see com.wxxr.mobile.core.ui.common.BindableBeanSupport#addPropertyChangeListener(com.wxxr.mobile.core.bean.api.IPropertyChangeListener)
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	/**
	 * @param listener
	 * @see com.wxxr.mobile.core.ui.common.BindableBeanSupport#removePropertyChangeListener(com.wxxr.mobile.core.bean.api.IPropertyChangeListener)
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
	
	private long currentTime = System.currentTimeMillis();
	private boolean ticking = true;

	/**
	 * @return the currentTime
	 */
	public long getCurrentTime() {
		return currentTime;
	}

	/**
	 * @return the ticking
	 */
	public boolean isTicking() {
		return ticking;
	}

	/**
	 * @param currentTime the currentTime to set
	 */
	public void setCurrentTime(long currentTime) {
		long old = this.currentTime;
		this.currentTime = currentTime;
		if(old != this.currentTime){
			this.support.firePropertyChange("currentTime", old, this.currentTime);
		}
	}

	/**
	 * @param ticking the ticking to set
	 */
	public void setTicking(boolean ticking) {
		boolean old = this.ticking;
		this.ticking = ticking;
		if(this.ticking != old){
			this.support.firePropertyChange("ticking", old, this.ticking);
		}
	}
	
	
}
