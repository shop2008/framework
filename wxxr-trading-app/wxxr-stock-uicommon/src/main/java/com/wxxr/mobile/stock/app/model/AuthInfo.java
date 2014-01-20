package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;

public class AuthInfo implements IBindableBean{

	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	/**用户名*/
	private String accountName;
	
	/**开户行*/
	private String bankName;
	
	/**开户行所在地*/
	private String bankAddr;
	
	/**银行卡号*/
	private String bankNum;

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		
		
		String old = this.accountName;
		this.accountName = accountName;
		this.emitter.firePropertyChange("accountName", old, this.accountName);
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
	//	this.bankName = bankName;
		
		String old = this.bankName;
		this.bankName = bankName;
		this.emitter.firePropertyChange("bankName", old, this.bankName);
	}

	/**
	 * @return the bankAddr
	 */
	public String getBankAddr() {
		return bankAddr;
	}

	/**
	 * @param bankAddr the bankAddr to set
	 */
	public void setBankAddr(String bankAddr) {
		//this.bankAddr = bankAddr;
		String old = this.bankAddr;
		this.bankAddr = bankAddr;
		this.emitter.firePropertyChange("bankAddr", old, this.bankAddr);
	}

	/**
	 * @return the bankNum
	 */
	public String getBankNum() {
		return bankNum;
	}

	/**
	 * @param bankNum the bankNum to set
	 */
	public void setBankNum(String bankNum) {
		//this.bankNum = bankNum;
		String old = this.bankNum;
		this.bankNum = bankNum;
		this.emitter.firePropertyChange("bankNum", old, this.bankNum);
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
