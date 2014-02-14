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
	/**是否已认证过*/
	private boolean confirmed;
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

	public boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		boolean old = this.confirmed;
		this.confirmed = confirmed;
		this.emitter.firePropertyChange("confirmed", old, this.confirmed);
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
	public String toString() {
		return "AuthInfo [accountName=" + accountName + ", bankName="
				+ bankName + ", bankAddr=" + bankAddr + ", bankNum=" + bankNum
				+ ", confirmed=" + confirmed + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accountName == null) ? 0 : accountName.hashCode());
		result = prime * result
				+ ((bankAddr == null) ? 0 : bankAddr.hashCode());
		result = prime * result
				+ ((bankName == null) ? 0 : bankName.hashCode());
		result = prime * result + ((bankNum == null) ? 0 : bankNum.hashCode());
		result = prime * result + (confirmed ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthInfo other = (AuthInfo) obj;
		if (accountName == null) {
			if (other.accountName != null)
				return false;
		} else if (!accountName.equals(other.accountName))
			return false;
		if (bankAddr == null) {
			if (other.bankAddr != null)
				return false;
		} else if (!bankAddr.equals(other.bankAddr))
			return false;
		if (bankName == null) {
			if (other.bankName != null)
				return false;
		} else if (!bankName.equals(other.bankName))
			return false;
		if (bankNum == null) {
			if (other.bankNum != null)
				return false;
		} else if (!bankNum.equals(other.bankNum))
			return false;
		if (confirmed != other.confirmed)
			return false;
		return true;
	}

	
	
	
}
