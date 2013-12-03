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
public class TradingRecordBean implements IBindableBean {
	
	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	private long fee;
	private long amount;
	private long vol;
	private long price;
	private String market;
	private long tax;
	private String describe;
	private long brokerage;
	private boolean beDone;
	private int day;
	private String code;
	private long date;
	private long id;
    private String acctID;

	public long getId() {
        return id;
    }

    public void setId(long id) {
        long old = this.id;
        this.id = id;
        this.emitter.firePropertyChange("id", old, this.id);
    }

    public String getAcctID() {
        return acctID;
    }

    public void setAcctID(String acctID) {
        String old = this.acctID;
        this.acctID = acctID;
        this.emitter.firePropertyChange("acctID", old, this.acctID);    }


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
	 * @return the fee
	 */
	public long getFee() {
		return fee;
	}

	/**
	 * @param fee the fee to set
	 */
	public void setFee(long fee) {
		long old = this.fee;
		this.fee = fee;
		this.emitter.firePropertyChange("fee", old, this.fee);
	}

	/**
	 * @return the amount
	 */
	public long getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(long amount) {
		long old = this.amount;
		this.amount = amount;
		this.emitter.firePropertyChange("amount", old, this.amount);
	}

	/**
	 * @return the vol
	 */
	public long getVol() {
		return vol;
	}

	/**
	 * @param vol the vol to set
	 */
	public void setVol(long vol) {
		long old = this.vol;
		this.vol = vol;
		this.emitter.firePropertyChange("vol", old, this.vol);
	}

	/**
	 * @return the price
	 */
	public long getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(long price) {
		long old = this.price;
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
	 * @return the tax
	 */
	public long getTax() {
		return tax;
	}

	/**
	 * @param tax the tax to set
	 */
	public void setTax(long tax) {
		long old = this.tax;
		this.tax = tax;
		this.emitter.firePropertyChange("tax", old, this.tax);
	}

	/**
	 * @return the describe
	 */
	public String getDescribe() {
		return describe;
	}

	/**
	 * @param describe the describe to set
	 */
	public void setDescribe(String describe) {
		String old = this.describe;
		this.describe = describe;
		this.emitter.firePropertyChange("describe", old, this.describe);
	}

	/**
	 * @return the brokerage
	 */
	public long getBrokerage() {
		return brokerage;
	}

	/**
	 * @param brokerage the brokerage to set
	 */
	public void setBrokerage(long brokerage) {
		long old = this.brokerage;
		this.brokerage = brokerage;
		this.emitter.firePropertyChange("brokerage", old, this.brokerage);
	}

	/**
	 * @return the beDone
	 */
	public boolean getBeDone() {
		return beDone;
	}

	/**
	 * @param beDone the beDone to set
	 */
	public void setBeDone(boolean beDone) {
		boolean old = this.beDone;
		this.beDone = beDone;
		this.emitter.firePropertyChange("beDone", old, this.beDone);
	}

	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(int day) {
		int old = this.day;
		this.day = day;
		this.emitter.firePropertyChange("day", old, this.day);
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
	public long getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(long date) {
		long old = this.date;
		this.date = date;
		this.emitter.firePropertyChange("date", old, this.date);
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override   
    public String toString() {
        return "TradingRecordBean ["+
                "fee=" + this.fee +
                " , amount=" + this.amount +
                " , vol=" + this.vol +
                " , price=" + this.price +
                " , market=" + this.market +
                " , tax=" + this.tax +
                " , describe=" + this.describe +
                " , brokerage=" + this.brokerage +
                " , beDone=" + this.beDone +
                " , day=" + this.day +
                " , code=" + this.code +
                " , date=" + this.date +
        "]";
    }	

}
