/**
 * 
 */
package com.wxxr.trading.core.storage.record.persistence.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wxxr.persistence.annotation.Persistence;
import com.wxxr.smbiz.bizobject.ILongKeyObject;
import com.wxxr.trading.core.model.AccountOperation;
import com.wxxr.trading.core.storage.account.bean.AssetAccountInfo;
import com.wxxr.trading.core.storage.record.persistence.ITradingRecordDAO;
import com.wxxr.trading.core.storage.trading.bean.TradingInfo;
import com.wxxr.trading.core.storage.transaction.persistence.bean.TransactionInfo;

/**
 * @author neillin
 *
 */
@Entity
@Table(name="TRADING_RECORD")
@Persistence(daoClass = ITradingRecordDAO.class)
@SequenceGenerator(name = "SEQ_TRADING_RECORD_ID", sequenceName = "SEQ_TRADING_RECORD_ID")
public class TradingRecordInfo implements ILongKeyObject {
	@Id
	@Column(name="ID")
	@GeneratedValue(generator = "SEQ_TRADING_RECORD_ID")
	private Long id;
		
	@Column(name="TYPE")
	@Enumerated(EnumType.STRING)
	private AccountOperation operation;
	
	@JoinColumn(name="ACCT_ID",nullable=false)
	private AssetAccountInfo account;
	
	@Column(name="OPERATOR",length=50)
	private String operator;
	
	@Column(name="TIMESTAMP")
	private Date timestamp;
	
	@Column(name="COMMENTS",length=1024)
	private String comments;
	
	@JoinColumn(name="TRADING_ID",nullable=false)
	private TradingInfo trading;

	@Column(name="VIRTUAL",nullable=false)
	private boolean virtual;

	@Column(name="EXTRA",nullable=true)
	private String extraInfo;
	
	@JoinColumn(name="TX_ID",nullable=false)
	private TransactionInfo transaction;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the account
	 */
	public AssetAccountInfo getAccount() {
		return account;
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
	 * @return the trading
	 */
	public TradingInfo getTrading() {
		return trading;
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
	 * @param account the account to set
	 */
	public void setAccount(AssetAccountInfo account) {
		this.account = account;
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
	 * @param trading the trading to set
	 */
	public void setTrading(TradingInfo trading) {
		this.trading = trading;
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
	 * @return the operation
	 */
	public AccountOperation getOperation() {
		return operation;
	}

	/**
	 * @return the transaction
	 */
	public TransactionInfo getTransaction() {
		return transaction;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(AccountOperation operation) {
		this.operation = operation;
	}

	/**
	 * @param transaction the transaction to set
	 */
	public void setTransaction(TransactionInfo transaction) {
		this.transaction = transaction;
	}

}
