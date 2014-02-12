package com.wxxr.mobile.stock.app.bean;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;

public class DrawMoneyRecordBean implements IBindableBean{

	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	private String drawDate;
	private Long drawAmount;
	private String drawState;
	private Long id;
	
	public String getDrawDate() {
		return drawDate;
	}
	/**
	 * @param drawDate the drawDate to set
	 */
	public void setDrawDate(String drawDate) {
		String old = this.drawDate;
		this.drawDate = drawDate;
		this.emitter.firePropertyChange("drawDate", old, this.drawDate);
	}
	/**
	 * @return the drawAmount
	 */
	public Long getDrawAmount() {
		return drawAmount;
	}
	/**
	 * @param drawAmount the drawAmount to set
	 */
	public void setDrawAmount(Long drawAmount) {
		Long old = this.drawAmount;
		this.drawAmount = drawAmount;
		this.emitter.firePropertyChange("drawAmount", old, this.drawAmount);
	}
	/**
	 * @return the drawState
	 */
	public String getDrawState() {
		return drawState;
	}
	/**
	 * @param drawState the drawState to set
	 */
	public void setDrawState(String drawState) {
		String old = this.drawState;
		this.drawState = drawState;
		this.emitter.firePropertyChange("drawState", old, this.drawState);
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		Long old = this.id;
		this.id = id;
		this.emitter.firePropertyChange("id", old, this.id);
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
