/**
 * 
 */
package com.wxxr.trading.core.storage.account.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wxxr.persistence.Auditable;
import com.wxxr.persistence.annotation.Persistence;
import com.wxxr.trading.core.storage.account.persistence.IFrozenAssetDAO;

/**
 * @author neillin
 *
 */
@Entity
@Table(name="FROZEN_RECORD")
@Persistence(daoClass = IFrozenAssetDAO.class)
@SequenceGenerator(name = "SEQ_FROZEN_RECD_ID", sequenceName = "SEQ_FROZEN_RECD_ID")
public class FrozenAssetInfo implements Auditable{
	@Id
	@Column(name="ID")
	@GeneratedValue(generator = "SEQ_FROZEN_RECD_ID")
	private Long id;
		
	@ManyToOne(optional=false)
	@JoinColumn(name="ASSET_ID")
	private AssetInfo asset;
	
	@Column(name="BALANCE")
	private long balance;
	
	@Column(name="TOTAL")
	private long totalAmount;
	
	@Column(name="COMMENTS",length=1024)
	private String comments;

	@Column(name="CREATED_BY",length=50)
	private String createdBy;
	
	@Column(name="UPDATED_BY",length=50)
	private String lastUpdatedBy;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="UPDATED_DATE")
	private Date lastUpdatedDate;
	
	@Column(name="CLOSED")
	private boolean closed;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @return the balance
	 */
	public long getBalance() {
		return balance;
	}

	/**
	 * @return the totalAmount
	 */
	public long getTotalAmount() {
		return totalAmount;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

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
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @param balance the balance to set
	 */
	public void setBalance(long balance) {
		this.balance = balance;
	}

	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
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
	 * @return the asset
	 */
	public AssetInfo getAsset() {
		return asset;
	}


	/**
	 * @param asset the asset to set
	 */
	public void setAsset(AssetInfo asset) {
		this.asset = asset;
	}

	
	

	/**
	 * @return the closed
	 */
	public boolean isClosed() {
		return closed;
	}


	/**
	 * @param closed the closed to set
	 */
	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FrozenRecordInfo [id=" + id + ", asset=" + asset + ", balance="
				+ balance + ", totalAmount=" + totalAmount + ", comments="
				+ comments + ", createdBy=" + createdBy + ", lastUpdatedBy="
				+ lastUpdatedBy + ", createdDate=" + createdDate
				+ ", lastUpdatedDate=" + lastUpdatedDate + ", closed=" + closed
				+ "]";
	}




}
