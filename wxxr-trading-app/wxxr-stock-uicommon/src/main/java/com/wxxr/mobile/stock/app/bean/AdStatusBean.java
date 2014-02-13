/**
 * 
 */
package com.wxxr.mobile.stock.app.bean;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;

/**
 * @author wangxuyang
 *
 */
public class AdStatusBean implements IBindableBean{
	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	private boolean off = false;

	public boolean getOff() {
		return off;
	}

	public void setOff(boolean off) {
		boolean old = this.off;
		this.off = off;
		this.emitter.firePropertyChange("off", old, this.off);
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
	
	

}
