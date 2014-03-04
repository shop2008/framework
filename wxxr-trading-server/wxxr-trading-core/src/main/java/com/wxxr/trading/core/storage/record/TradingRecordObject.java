/**
 * 
 */
package com.wxxr.trading.core.storage.record;

import java.util.Date;

import com.wxxr.trading.core.model.AccountOperation;
import com.wxxr.trading.core.model.ITradingRecord;

/**
 * @author neillin
 *
 */
public abstract class TradingRecordObject implements ITradingRecord {

	private Long id;
	
	private AccountOperation operation;
	
	private Long accountId;
	
	private String operator;
	
	private Date timestamp;
	
	private String comments;
	
	private Long tradingId;

	private boolean virtual;

	private String extraInfo;
	
	private Long transactionId;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @return the tradingId
	 */
	public Long getTradingId() {
		return tradingId;
	}

	/**
	 * @return the virtual
	 */
	public boolean isVirtual() {
		return virtual;
	}

	/**
	 * @return the extraInfo
	 */
	public String getExtraInfo() {
		return extraInfo;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @param tradingId the tradingId to set
	 */
	public void setTradingId(Long tradingId) {
		this.tradingId = tradingId;
	}

	/**
	 * @param virtual the virtual to set
	 */
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

	/**
	 * @param extraInfo the extraInfo to set
	 */
	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	/**
	 * @return the transactionId
	 */
	public Long getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the operation
	 */
	public AccountOperation getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(AccountOperation operation) {
		this.operation = operation;
	}

	@Override
	public String getType() {
		return this.operation.name();
	}


}
