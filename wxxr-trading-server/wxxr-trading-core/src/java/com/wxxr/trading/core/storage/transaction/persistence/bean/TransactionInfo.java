/**
 * 
 */
package com.wxxr.trading.core.storage.transaction.persistence.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wxxr.persistence.annotation.Persistence;
import com.wxxr.smbiz.bizobject.ILongKeyObject;
import com.wxxr.trading.core.storage.account.TxStatus;
import com.wxxr.trading.core.storage.transaction.persistence.ITransactionInfoDAO;

/**
 * @author wangyan
 *
 */
@Entity
@Table(name="TX_INFO")
@Persistence(daoClass = ITransactionInfoDAO.class)
@SequenceGenerator(name = "SEQ_TX_INFO_ID", sequenceName = "SEQ_TX_INFO_ID") 
public class TransactionInfo implements ILongKeyObject {
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator = "SEQ_TX_INFO_ID")
	private Long id;
	
	@Column(name="TRADING_ID")
	private Long tradingId;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="TX_CODE")
	private String transactionCode;
	
	@Column(name="CREATED_TIME")
	private Date createdTime;
	
	@Column(name="COMPLETED_TIME")
	private Date completedTime;
	
	@Column(name="STATUS")
	private TxStatus status;

//	@OneToMany(cascade=CascadeType.ALL,mappedBy="operationId")
//	private List<TradingRecordInfo> records;
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
	 * @return the tradingId
	 */
	public Long getTradingId() {
		return tradingId;
	}

	/**
	 * @param tradingId the tradingId to set
	 */
	public void setTradingId(Long tradingId) {
		this.tradingId = tradingId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the transactionCode
	 */
	public String getTransactionCode() {
		return transactionCode;
	}

	/**
	 * @param transactionCode the transactionCode to set
	 */
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TransactionInfo [id=" + id + ", tradingId=" + tradingId
				+ ", description=" + description + ", transactionCode="
				+ transactionCode + "]";
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
