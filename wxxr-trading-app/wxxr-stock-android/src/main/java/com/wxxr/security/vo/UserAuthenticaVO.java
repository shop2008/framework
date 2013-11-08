package com.wxxr.security.vo;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UserAuthenVO")
public class UserAuthenticaVO implements Serializable{
	
	private static final long serialVersionUID = -3628688505113496679L;
	@XmlElement(name = "acctName")
	private String acctName;
	@XmlElement(name = "acctBank")
	private String acctBank;
	@XmlElement(name = "bankPosition")
	private String bankPosition;
	@XmlElement(name = "bankNum")
	private String bankNum;

	/**
	 * @return the acctName
	 */
	
	public String getAcctName() {
		return acctName;
	}

	/**
	 * @param acctName the acctName to set
	 */
	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}

	/**
	 * @return the acctBank
	 */
	
	public String getAcctBank() {
		return acctBank;
	}

	/**
	 * @param acctBank the acctBank to set
	 */
	public void setAcctBank(String acctBank) {
		this.acctBank = acctBank;
	}

	/**
	 * @return the bankPosition
	 */
	
	public String getBankPosition() {
		return bankPosition;
	}

	/**
	 * @param bankPosition the bankPosition to set
	 */
	public void setBankPosition(String bankPosition) {
		this.bankPosition = bankPosition;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserAuthenticaVO [acctName=" + acctName + ", acctBank=" + acctBank + ", bankPosition=" + bankPosition + ", bankNum=" + bankNum + "]";
	}
	
	
}
