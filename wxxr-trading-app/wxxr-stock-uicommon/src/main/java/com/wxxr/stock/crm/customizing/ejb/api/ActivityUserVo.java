package com.wxxr.stock.crm.customizing.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ActivityUser")
public class ActivityUserVo implements Serializable{
	
	private static final long serialVersionUID = -3767859549226728231L;
	@XmlElement(name = "id")
	private Long id;
	@XmlElement(name = "userId")
	private String userId;// 用户ID
	@XmlElement(name = "balance")
	private Long balance;// 用户余额
	@XmlElement(name = "voucherVol")
	private Long voucherVol;// 实盘券数量
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
		this.id = id;
	}
	/**
	 * @return the userId
	 */
	
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the balance
	 */
	public Long getBalance() {
		return balance;
	}
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(Long balance) {
		this.balance = balance;
	}
	/**
	 * @return the voucherVol
	 */
	public Long getVoucherVol() {
		return voucherVol;
	}
	/**
	 * @param voucherVol the voucherVol to set
	 */
	public void setVoucherVol(Long voucherVol) {
		this.voucherVol = voucherVol;
	}

}
