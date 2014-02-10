/**
 * 
 */
package com.wxxr.trading.core.storage.transaction;

import java.util.Date;

import com.wxxr.trading.core.model.ITradingTransaction;
import com.wxxr.trading.core.storage.account.TxStatus;

/**
 * @author neillin
 *
 */
public abstract class AbstractTransaction implements ITradingTransaction {

	private String type, description,operationCode;
	private Long tradingId, id;
	private Date createdTime, completedTime;
	private TxStatus status;
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return the operationCode
	 */
	public String getOperationCode() {
		return operationCode;
	}
	/**
	 * @return the tradingId
	 */
	public Long getTradingId() {
		return tradingId;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @param operationCode the operationCode to set
	 */
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	/**
	 * @param tradingId the tradingId to set
	 */
	public void setTradingId(Long tradingId) {
		this.tradingId = tradingId;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * @return the status
	 */
	public TxStatus getStatus() {
		return status;
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
	/**
	 * @param status the status to set
	 */
	public void setStatus(TxStatus status) {
		this.status = status;
	}
}
