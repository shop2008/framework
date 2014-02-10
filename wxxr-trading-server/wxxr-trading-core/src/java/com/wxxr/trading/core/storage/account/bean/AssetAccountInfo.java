/**
 * 
 */
package com.wxxr.trading.core.storage.account.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wxxr.persistence.Auditable;
import com.wxxr.persistence.annotation.Persistence;
import com.wxxr.smbiz.bizobject.IAuditableLKeyObject;
import com.wxxr.trading.core.storage.account.persistence.IAssetAccountDAO;

/**
 * @author neillin
 *
 */
@Entity
@Table(name="ACCT_INFO")
@Persistence(daoClass = IAssetAccountDAO.class)
@SequenceGenerator(name = "SEQ_ACCOUNT_ID", sequenceName = "SEQ_ACCOUNT_ID",initialValue=1000)
public class AssetAccountInfo implements IAuditableLKeyObject,Auditable {
	
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
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator = "SEQ_ACCOUNT_ID")
	private Long id;
		
	@Column(name="TYPE",length=20,nullable=false)
	private String type;

	@OneToMany(cascade=CascadeType.ALL,mappedBy="account")
	private List<AssetInfo> allAssets = new ArrayList<AssetInfo>();
	
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	
	/**
	 * @return the allAssets
	 */
	public List<AssetInfo> getAllAssets() {
		return allAssets;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AssetAccountInfo [createdBy=" + createdBy + ", lastUpdatedBy="
				+ lastUpdatedBy + ", createdDate=" + createdDate
				+ ", lastUpdatedDate=" + lastUpdatedDate + ", ownerId="
				+ ownerId + ", id=" + id + ", type=" + type + "]";
	}
	
	
}
