package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "UserSignVO")
public class UserSignVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Boolean sign;
	private long ongoingDays;
	private long rewardVol;
	private String signDate;
	private Integer success;//失败为-1，重复签到为：0，成功为：1；
	private String failReason;
	/**
	 * @return the isSign
	 */
	@XmlElement
	public Boolean isSign() {
		return sign;
	}
	/**
	 * @param isSign the isSign to set
	 */
	public void setSign(Boolean isSign) {
		this.sign = isSign;
	}
	/**
	 * @return the ongoingDays
	 */
	@XmlElement
	public long getOngoingDays() {
		return ongoingDays;
	}
	/**
	 * @param ongoingDays the ongoingDays to set
	 */
	public void setOngoingDays(long ongoingDays) {
		this.ongoingDays = ongoingDays;
	}
	/**
	 * @return the acquireVol
	 */
	@XmlElement
	public long getRewardVol() {
		return rewardVol;
	}
	/**
	 * @param acquireVol the acquireVol to set
	 */
	public void setRewardVol(long rewardVol) {
		this.rewardVol = rewardVol;
	}
	/**
	 * @return the signDate
	 */
	@XmlElement
	public String getSignDate() {
		return signDate;
	}
	/**
	 * @param signDate the signDate to set
	 */
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	
	/**
	 * @return the success
	 */
	@XmlElement
	public Integer getSuccess() {
		return success;
	}
	/**
	 * @param success the success to set
	 */
	public void setSuccess(Integer success) {
		this.success = success;
	}
	/**
	 * @return the failReason
	 */
	@XmlElement
	public String getFailReason() {
		return failReason;
	}
	/**
	 * @param failReason the failReason to set
	 */
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserSignVO [sign=" + sign + ", ongoingDays=" + ongoingDays + ", rewardVol=" + rewardVol + ", signDate=" + signDate + ", success=" + success
				+ ", failReason=" + failReason + "]";
	}
	
}
