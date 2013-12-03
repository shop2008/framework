/*
 * Generated code, don't modified !
 */
package com.wxxr.mobile.stock.app.bean;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;

/**
 * Generated by Bindable Bean generator
 *
 */
public class StockLineBean implements IBindableBean {
	
	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	private Long limit;
	private String code;
	private String date;
	private Long secuvolume;
	private Long close;
	private Long open;
	private String time;
	private Long price;
	private String market;
	private Long start;
	private Long high;
	private Long secuamount;
	private Long low;

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



	/**
	 * @return the limit
	 */
	public Long getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(Long limit) {
		Long old = this.limit;
		this.limit = limit;
		this.emitter.firePropertyChange("limit", old, this.limit);
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		String old = this.code;
		this.code = code;
		this.emitter.firePropertyChange("code", old, this.code);
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		String old = this.date;
		this.date = date;
		this.emitter.firePropertyChange("date", old, this.date);
	}

	/**
	 * @return the secuvolume
	 */
	public Long getSecuvolume() {
		return secuvolume;
	}

	/**
	 * @param secuvolume the secuvolume to set
	 */
	public void setSecuvolume(Long secuvolume) {
		Long old = this.secuvolume;
		this.secuvolume = secuvolume;
		this.emitter.firePropertyChange("secuvolume", old, this.secuvolume);
	}

	/**
	 * @return the close
	 */
	public Long getClose() {
		return close;
	}

	/**
	 * @param close the close to set
	 */
	public void setClose(Long close) {
		Long old = this.close;
		this.close = close;
		this.emitter.firePropertyChange("close", old, this.close);
	}

	/**
	 * @return the open
	 */
	public Long getOpen() {
		return open;
	}

	/**
	 * @param open the open to set
	 */
	public void setOpen(Long open) {
		Long old = this.open;
		this.open = open;
		this.emitter.firePropertyChange("open", old, this.open);
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		String old = this.time;
		this.time = time;
		this.emitter.firePropertyChange("time", old, this.time);
	}

	/**
	 * @return the price
	 */
	public Long getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Long price) {
		Long old = this.price;
		this.price = price;
		this.emitter.firePropertyChange("price", old, this.price);
	}

	/**
	 * @return the market
	 */
	public String getMarket() {
		return market;
	}

	/**
	 * @param market the market to set
	 */
	public void setMarket(String market) {
		String old = this.market;
		this.market = market;
		this.emitter.firePropertyChange("market", old, this.market);
	}

	/**
	 * @return the start
	 */
	public Long getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Long start) {
		Long old = this.start;
		this.start = start;
		this.emitter.firePropertyChange("start", old, this.start);
	}

	/**
	 * @return the high
	 */
	public Long getHigh() {
		return high;
	}

	/**
	 * @param high the high to set
	 */
	public void setHigh(Long high) {
		Long old = this.high;
		this.high = high;
		this.emitter.firePropertyChange("high", old, this.high);
	}

	/**
	 * @return the secuamount
	 */
	public Long getSecuamount() {
		return secuamount;
	}

	/**
	 * @param secuamount the secuamount to set
	 */
	public void setSecuamount(Long secuamount) {
		Long old = this.secuamount;
		this.secuamount = secuamount;
		this.emitter.firePropertyChange("secuamount", old, this.secuamount);
	}

	/**
	 * @return the low
	 */
	public Long getLow() {
		return low;
	}

	/**
	 * @param low the low to set
	 */
	public void setLow(Long low) {
		Long old = this.low;
		this.low = low;
		this.emitter.firePropertyChange("low", old, this.low);
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override   
    public String toString() {
        return "StockLineBean ["+
                "limit=" + this.limit +
                " , code=" + this.code +
                " , date=" + this.date +
                " , secuvolume=" + this.secuvolume +
                " , close=" + this.close +
                " , open=" + this.open +
                " , time=" + this.time +
                " , price=" + this.price +
                " , market=" + this.market +
                " , start=" + this.start +
                " , high=" + this.high +
                " , secuamount=" + this.secuamount +
                " , low=" + this.low +
        "]";
    }	

}
