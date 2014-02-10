/**
 * 
 */
package com.wxxr.trading.core.api;

import java.util.List;

import com.wxxr.trading.core.model.ITradingTransaction;
import com.wxxr.trading.core.model.ITradingRecord;

/**
 * @author wangyan
 *
 */
public abstract class AbstractTradingOpeartion implements ITradingTransaction {

	private Long operationId;
	private Long tradingId;
	private String description;
	private List<ITradingRecord> details;
	private String operationCode;
	
	/**
	 * @return the operationId
	 */
	public Long getOperationId() { 
		return operationId;
	}

	/**
	 * @return the tradingId
	 */
	public Long getTradingId() {
		return tradingId;
	}



	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}



	/**
	 * @param operationId the operationId to set
	 */
	public void setOperationId(Long operationId) {
		this.operationId = operationId;
	}

	/**
	 * @param tradingId the tradingId to set
	 */
	public void setTradingId(Long tradingId) {
		this.tradingId = tradingId;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the details
	 */
	public List<ITradingRecord> getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(List<ITradingRecord> details) {
		this.details = details;
	}

	/**
	 * @return the operationCode
	 */
	public String getOperationCode() {
		return operationCode;
	}

	/**
	 * @param operationCode the operationCode to set
	 */
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return " operationId=" + operationId
				+ ", tradingId=" + tradingId + ", operationCode="
				+ operationCode + ", description=" + description + ", details="
				+ details + " ";
	}

	
	
}
