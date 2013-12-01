package com.wxxr.mobile.stock.app.model;

public class AuthInfo {

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
		this.accountName = accountName;
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
		this.bankName = bankName;
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
		this.bankAddr = bankAddr;
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
		this.bankNum = bankNum;
	}
	
	
}
