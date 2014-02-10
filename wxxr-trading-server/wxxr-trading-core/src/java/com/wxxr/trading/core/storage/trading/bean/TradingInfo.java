/**
 * 
 */
package com.wxxr.trading.core.storage.trading.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wxxr.persistence.annotation.Persistence;
import com.wxxr.smbiz.bizobject.IAuditableLKeyObject;
import com.wxxr.trading.core.model.TradingStatus;
import com.wxxr.trading.core.storage.trading.persistence.ITradingInfoDAO;
import com.wxxr.trading.core.storage.tradingcode.persistence.bean.TradingCodeInfo;

/**
 * @author neillin
 *
 */
@Entity
@Table(name="TRADING_INFO")
@Persistence(daoClass = ITradingInfoDAO.class)
@SequenceGenerator(name = "SEQ_TRADING_ID", sequenceName = "SEQ_TRADING_ID",initialValue=100)
public class TradingInfo implements IAuditableLKeyObject {

	@Column(name="CREATED_BY",length=50)
	private String createdBy;
	
	@Column(name="UPDATED_BY",length=50)
	private String lastUpdatedBy;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="UPDATED_DATE")
	private Date lastUpdatedDate;
	
	@Column(name="OWNER",length=50,nullable=false)
	private String ownerId;
	
	@Column(name="COMPLETED_TIME")
	private Date completedTime;

	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator = "SEQ_TRADING_ID")
	private Long id;
			
	@Column(name="BROKER",length=50)
	private String broker;

	@OneToMany(cascade=CascadeType.ALL,mappedBy="trading")
	private List<TradingPartyInfo> tradingParties = new ArrayList<TradingPartyInfo>();
	
	@Column(name="STATUS",length=20,nullable=false)
	private TradingStatus status;
	
	@ManyToOne
	@JoinColumn(name="T_CODE",nullable=false)
	private TradingCodeInfo tradingCode;
	
	@Column(name="SUB_STATUS",length=20)
	private String subStatus;

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @return the lastUpdatedBy
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @return the lastUpdatedDate
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	/**
	 * @return the ownerId
	 */
	public String getOwnerId() {
		return ownerId;
	}

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
		return getTradingCode().getCode();
	}

	/**
	 * @return the broker
	 */
	public String getBroker() {
		return broker;
	}

	/**
	 * @return the status
	 */
	public TradingStatus getStatus() {
		return status;
	}

	/**
	 * @return the tradingCode
	 */
	public TradingCodeInfo getTradingCode() {
		return tradingCode;
	}

	/**
	 * @return the subStatus
	 */
	public String getSubStatus() {
		return subStatus;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @param lastUpdatedBy the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @param lastUpdatedDate the lastUpdatedDate to set
	 */
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @param broker the broker to set
	 */
	public void setBroker(String broker) {
		this.broker = broker;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(TradingStatus status) {
		this.status = status;
	}

	/**
	 * @param tradingCode the tradingCode to set
	 */
	public void setTradingCode(TradingCodeInfo tradingCode) {
		this.tradingCode = tradingCode;
	}

	/**
	 * @param subStatus the subStatus to set
	 */
	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}

	/**
	 * @return the tradingParties
	 */
	public List<TradingPartyInfo> getTradingParties() {
		return tradingParties;
	}

	/**
	 * @return the completedTime
	 */
	public Date getCompletedTime() {
		return completedTime;
	}

	/**
	 * @param completedTime the completedTime to set
	 */
	public void setCompletedTime(Date completedTime) {
		this.completedTime = completedTime;
	}

}
