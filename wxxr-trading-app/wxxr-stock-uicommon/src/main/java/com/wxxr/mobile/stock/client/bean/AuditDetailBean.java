/*
 * Generated code, don't modified !
 */
package com.wxxr.mobile.stock.client.bean;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.PropertyChangeListener;
import com.wxxr.mobile.core.bean.api.PropertyChangeSupport;

/**
 * Generated by Bindable Bean generator
 *
 */
public class AuditDetailBean implements IBindableBean {
	
	private final PropertyChangeSupport emitter = new PropertyChangeSupport(this);
	private boolean virtual;
	private String tradingCost;
	private String frozenAmount;
	private String accountPay;
	private String payOut;
	private String buyDay;
	private String userGain;
	private String type;
	private String cost;
	private String buyAverage;
	private String id;
	private String sellAverage;
	private String unfreezeAmount;
	private String totalGain;
	private String capitalRate;
	private String fund;
	private String deadline;
	private String tradingDate;
	private String plRisk;

	/**
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		emitter.addPropertyChangeListener(listener);
	}

	/**
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		emitter.removePropertyChangeListener(listener);
	}

	/**
	 * @param propertyName
	 * @param listener
	 */
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		emitter.addPropertyChangeListener(propertyName, listener);
	}

	/**
	 * @param propertyName
	 * @param listener
	 */
	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		emitter.removePropertyChangeListener(propertyName, listener);
	}


	/**
	 * @return the virtual
	 */
	public boolean getVirtual() {
		return virtual;
	}

	/**
	 * @param virtual the virtual to set
	 */
	public void setVirtual(boolean virtual) {
		boolean old = this.virtual;
		this.virtual = virtual;
		this.emitter.firePropertyChange("virtual", old, this.virtual);
	}

	/**
	 * @return the tradingCost
	 */
	public String getTradingCost() {
		return tradingCost;
	}

	/**
	 * @param tradingCost the tradingCost to set
	 */
	public void setTradingCost(String tradingCost) {
		String old = this.tradingCost;
		this.tradingCost = tradingCost;
		this.emitter.firePropertyChange("tradingCost", old, this.tradingCost);
	}

	/**
	 * @return the frozenAmount
	 */
	public String getFrozenAmount() {
		return frozenAmount;
	}

	/**
	 * @param frozenAmount the frozenAmount to set
	 */
	public void setFrozenAmount(String frozenAmount) {
		String old = this.frozenAmount;
		this.frozenAmount = frozenAmount;
		this.emitter.firePropertyChange("frozenAmount", old, this.frozenAmount);
	}

	/**
	 * @return the accountPay
	 */
	public String getAccountPay() {
		return accountPay;
	}

	/**
	 * @param accountPay the accountPay to set
	 */
	public void setAccountPay(String accountPay) {
		String old = this.accountPay;
		this.accountPay = accountPay;
		this.emitter.firePropertyChange("accountPay", old, this.accountPay);
	}

	/**
	 * @return the payOut
	 */
	public String getPayOut() {
		return payOut;
	}

	/**
	 * @param payOut the payOut to set
	 */
	public void setPayOut(String payOut) {
		String old = this.payOut;
		this.payOut = payOut;
		this.emitter.firePropertyChange("payOut", old, this.payOut);
	}

	/**
	 * @return the buyDay
	 */
	public String getBuyDay() {
		return buyDay;
	}

	/**
	 * @param buyDay the buyDay to set
	 */
	public void setBuyDay(String buyDay) {
		String old = this.buyDay;
		this.buyDay = buyDay;
		this.emitter.firePropertyChange("buyDay", old, this.buyDay);
	}

	/**
	 * @return the userGain
	 */
	public String getUserGain() {
		return userGain;
	}

	/**
	 * @param userGain the userGain to set
	 */
	public void setUserGain(String userGain) {
		String old = this.userGain;
		this.userGain = userGain;
		this.emitter.firePropertyChange("userGain", old, this.userGain);
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		String old = this.type;
		this.type = type;
		this.emitter.firePropertyChange("type", old, this.type);
	}

	/**
	 * @return the cost
	 */
	public String getCost() {
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(String cost) {
		String old = this.cost;
		this.cost = cost;
		this.emitter.firePropertyChange("cost", old, this.cost);
	}

	/**
	 * @return the buyAverage
	 */
	public String getBuyAverage() {
		return buyAverage;
	}

	/**
	 * @param buyAverage the buyAverage to set
	 */
	public void setBuyAverage(String buyAverage) {
		String old = this.buyAverage;
		this.buyAverage = buyAverage;
		this.emitter.firePropertyChange("buyAverage", old, this.buyAverage);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		String old = this.id;
		this.id = id;
		this.emitter.firePropertyChange("id", old, this.id);
	}

	/**
	 * @return the sellAverage
	 */
	public String getSellAverage() {
		return sellAverage;
	}

	/**
	 * @param sellAverage the sellAverage to set
	 */
	public void setSellAverage(String sellAverage) {
		String old = this.sellAverage;
		this.sellAverage = sellAverage;
		this.emitter.firePropertyChange("sellAverage", old, this.sellAverage);
	}

	/**
	 * @return the unfreezeAmount
	 */
	public String getUnfreezeAmount() {
		return unfreezeAmount;
	}

	/**
	 * @param unfreezeAmount the unfreezeAmount to set
	 */
	public void setUnfreezeAmount(String unfreezeAmount) {
		String old = this.unfreezeAmount;
		this.unfreezeAmount = unfreezeAmount;
		this.emitter.firePropertyChange("unfreezeAmount", old, this.unfreezeAmount);
	}

	/**
	 * @return the totalGain
	 */
	public String getTotalGain() {
		return totalGain;
	}

	/**
	 * @param totalGain the totalGain to set
	 */
	public void setTotalGain(String totalGain) {
		String old = this.totalGain;
		this.totalGain = totalGain;
		this.emitter.firePropertyChange("totalGain", old, this.totalGain);
	}

	/**
	 * @return the capitalRate
	 */
	public String getCapitalRate() {
		return capitalRate;
	}

	/**
	 * @param capitalRate the capitalRate to set
	 */
	public void setCapitalRate(String capitalRate) {
		String old = this.capitalRate;
		this.capitalRate = capitalRate;
		this.emitter.firePropertyChange("capitalRate", old, this.capitalRate);
	}

	/**
	 * @return the fund
	 */
	public String getFund() {
		return fund;
	}

	/**
	 * @param fund the fund to set
	 */
	public void setFund(String fund) {
		String old = this.fund;
		this.fund = fund;
		this.emitter.firePropertyChange("fund", old, this.fund);
	}

	/**
	 * @return the deadline
	 */
	public String getDeadline() {
		return deadline;
	}

	/**
	 * @param deadline the deadline to set
	 */
	public void setDeadline(String deadline) {
		String old = this.deadline;
		this.deadline = deadline;
		this.emitter.firePropertyChange("deadline", old, this.deadline);
	}

	/**
	 * @return the tradingDate
	 */
	public String getTradingDate() {
		return tradingDate;
	}

	/**
	 * @param tradingDate the tradingDate to set
	 */
	public void setTradingDate(String tradingDate) {
		String old = this.tradingDate;
		this.tradingDate = tradingDate;
		this.emitter.firePropertyChange("tradingDate", old, this.tradingDate);
	}

	/**
	 * @return the plRisk
	 */
	public String getPlRisk() {
		return plRisk;
	}

	/**
	 * @param plRisk the plRisk to set
	 */
	public void setPlRisk(String plRisk) {
		String old = this.plRisk;
		this.plRisk = plRisk;
		this.emitter.firePropertyChange("plRisk", old, this.plRisk);
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override   
    public String toString() {
        return "AuditDetailBean ["+
                "virtual=" + this.virtual +
                " , tradingCost=" + this.tradingCost +
                " , frozenAmount=" + this.frozenAmount +
                " , accountPay=" + this.accountPay +
                " , payOut=" + this.payOut +
                " , buyDay=" + this.buyDay +
                " , userGain=" + this.userGain +
                " , type=" + this.type +
                " , cost=" + this.cost +
                " , buyAverage=" + this.buyAverage +
                " , id=" + this.id +
                " , sellAverage=" + this.sellAverage +
                " , unfreezeAmount=" + this.unfreezeAmount +
                " , totalGain=" + this.totalGain +
                " , capitalRate=" + this.capitalRate +
                " , fund=" + this.fund +
                " , deadline=" + this.deadline +
                " , tradingDate=" + this.tradingDate +
                " , plRisk=" + this.plRisk +
        "]";
    }	

}
