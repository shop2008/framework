package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PersonalHomePageVO")
public class PersonalHomePageVO implements Serializable {

	private static final long serialVersionUID = 1L;
	@XmlElement(name="userID")
	private String userId; //id
	@XmlElement(name = "voucherVol")
	private Long voucherVol;
	@XmlElement(name = "totalProfit")
	private double totalProfit;
	@XmlElement(name = "actualCount")
	private long actualCount;
	@XmlElement(name = "virtualCount")
	private long virtualCount;
	@XStreamImplicit(itemFieldName="actualList")
	private List<GainVO> actualList;
	@XStreamImplicit(itemFieldName= "virtualList")
	private List<GainVO> virtualList;
	
	
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
	/**
	 * @return the totalProfit
	 */
	
	public double getTotalProfit() {
		return totalProfit;
	}
	/**
	 * @param totalProfit the totalProfit to set
	 */
	public void setTotalProfit(double totalProfit) {
		this.totalProfit = totalProfit;
	}
	/**
	 * @return the actualCount
	 */
	
	public long getActualCount() {
		return actualCount;
	}
	/**
	 * @param actualCount the actualCount to set
	 */
	public void setActualCount(long actualCount) {
		this.actualCount = actualCount;
	}
	/**
	 * @return the virtualCount
	 */
	
	public long getVirtualCount() {
		return virtualCount;
	}
	/**
	 * @param virtualCount the virtualCount to set
	 */
	public void setVirtualCount(long virtualCount) {
		this.virtualCount = virtualCount;
	}
	/**
	 * @return the actualList
	 */
	
	public List<GainVO> getActualList() {
		return actualList;
	}
	/**
	 * @param actualList the actualList to set
	 */
	public void setActualList(List<GainVO> actualList) {
		this.actualList = actualList;
	}
	/**
	 * @return the virtualList
	 */
	
	public List<GainVO> getVirtualList() {
		return virtualList;
	}
	/**
	 * @param virtualList the virtualList to set
	 */
	public void setVirtualList(List<GainVO> virtualList) {
		this.virtualList = virtualList;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PersonalHomePageVO [voucherVol=" + voucherVol + ", totalProfit=" + totalProfit + ", actualCount=" + actualCount + ", virtualCount="
				+ virtualCount + ", actualList=" + actualList + ", virtualList=" + virtualList + "]";
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}
