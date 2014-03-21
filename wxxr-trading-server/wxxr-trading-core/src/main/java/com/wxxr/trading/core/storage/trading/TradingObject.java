/**
 * 
 */
package com.wxxr.trading.core.storage.trading;

import java.util.Date;

import com.wxxr.trading.core.model.ITrading;
import com.wxxr.trading.core.model.TradingStatus;

/**
 * @author neillin
 *
 */
public abstract class TradingObject implements ITrading {

	private Long id;
	private String type;
	private Integer tradingCode;
	private TradingStatus status;
	private String subStatus;
	private String ownerId;
	private String broker;
	private Long[] tradingParties;
	private Date createdTime, completedTime;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @return the tradingCode
	 */
	public Integer getTradingCode() {
		return tradingCode;
	}
	/**
	 * @return the status
	 */
	public TradingStatus getStatus() {
		return status;
	}
	/**
	 * @return the subStatus
	 */
	public String getSubStatus() {
		return subStatus;
	}
	/**
	 * @return the ownerId
	 */
	public String getOwnerId() {
		return ownerId;
	}
	/**
	 * @return the broker
	 */
	public String getBroker() {
		return broker;
	}
	/**
	 * @return the tradingParties
	 */
	public Long[] getTradingParties() {
		return tradingParties;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @param tradingCode the tradingCode to set
	 */
	public void setTradingCode(Integer tradingCode) {
		this.tradingCode = tradingCode;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(TradingStatus status) {
		this.status = status;
	}
	/**
	 * @param subStatus the subStatus to set
	 */
	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}
	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	/**
	 * @param broker the broker to set
	 */
	public void setBroker(String broker) {
		this.broker = broker;
	}
	/**
	 * @param tradingParties the tradingParties to set
	 */
	public void setTradingParties(Long[] tradingParties) {
		this.tradingParties = tradingParties;
	}
	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}
	/**
	 * @return the completedTime
	 */
	public Date getCompletedTime() {
		return completedTime;
	}
	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	/**
	 * @param completedTime the completedTime to set
	 */
	public void setCompletedTime(Date completedTime) {
		this.completedTime = completedTime;
	}
	

}
