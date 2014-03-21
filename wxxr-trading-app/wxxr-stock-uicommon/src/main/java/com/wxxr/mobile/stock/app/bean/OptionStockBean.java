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
public class OptionStockBean implements IBindableBean{
	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	private String stockCode;
	private String mc;
	private  Integer power;
	private Long newPrice;
	private Long risefallrate;
	
	
	
	
	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		String old = this.stockCode;
		this.stockCode = stockCode;
		this.emitter.firePropertyChange("stockCode", old, this.stockCode);
	}

	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		String old = this.mc;
		this.mc = mc;
		this.emitter.firePropertyChange("mc", old, this.mc);
	}
	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		Integer old = this.power;
		this.power = power;
		this.emitter.firePropertyChange("power", old, this.power);
	}

	public Long getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(Long newPrice) {
		Long old = this.newPrice;
		this.newPrice = newPrice;
		this.emitter.firePropertyChange("newPrice", old, this.newPrice);
	}

	public Long getRisefallrate() {
		return risefallrate;
	}

	public void setRisefallrate(Long risefallrate) {
		Long old = this.risefallrate;
		this.risefallrate = risefallrate;
		this.emitter.firePropertyChange("risefallrate", old, this.risefallrate);
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

}
