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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wxxr.persistence.Auditable;
import com.wxxr.persistence.annotation.Persistence;
import com.wxxr.smbiz.bizobject.IAuditableLKeyObject;
import com.wxxr.trading.core.storage.account.persistence.IAssetDAO;

/**
 * @author neillin
 *
 */
@Entity
@Table(name="ASSET_INFO")
@Persistence(daoClass = IAssetDAO.class)
@SequenceGenerator(name = "SEQ_ACCOUNT_ASSET_ID", sequenceName = "SEQ_ACCOUNT_ASSET_ID",initialValue=100)
public class AssetInfo implements IAuditableLKeyObject,Auditable{
	@Id
	@Column(name="ID")
	@GeneratedValue(generator = "SEQ_ACCOUNT_ASSET_ID")
	private Long id;
		
	@Column(name="TYPE",length=20,nullable=false)
	private String type;

	@ManyToOne
	@JoinColumn(name="ACCT_ID",nullable=false)
	private AssetAccountInfo account;
	
	@Column(name="BALANCE")
	private long balance;
		
	/**
	 * ÊÇ·ñÐéÄâ×Ê²ú
	 * @return
	 */
	@Column(name="VIRTUAL",nullable=false)
	private boolean virtual;

	
	@Column(name="CREATED_BY",length=50)
	private String createdBy;
	
	@Column(name="UPDATED_BY",length=50)
	private String lastUpdatedBy;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="UPDATED_DATE")
	private Date lastUpdatedDate;
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="asset")
	private List<FrozenAssetInfo> frozenRecords = new ArrayList<FrozenAssetInfo>();

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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the balance
	 */
	public long getBalance() {
		return balance;
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
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(long balance) {
		this.balance = balance;
	}

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
	 * @return the virtual
	 */
	public boolean isVirtual() {
		return virtual;
	}

	/**
	 * @param virtual the virtual to set
	 */
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

	/**
	 * @return the account
	 */
	public AssetAccountInfo getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(AssetAccountInfo account) {
		this.account = account;
	}

	/**
	 * @return the frozenRecords
	 */
	public List<FrozenAssetInfo> getFrozenRecords() {
		return frozenRecords;
	}

	/**
	 * @param frozenRecords the frozenRecords to set
	 */
	public void setFrozenRecords(List<FrozenAssetInfo> frozenRecords) {
		this.frozenRecords = frozenRecords;
	}
	
	public void addFrozenRecord(FrozenAssetInfo info) {
		if(info == null){
			return;
		}
		if(this.frozenRecords == null){
			this.frozenRecords = new ArrayList<FrozenAssetInfo>();
		}
		for (FrozenAssetInfo recd : this.frozenRecords) {
			if(recd.getId().equals(info.getId())){
				return;
			}
		}
		this.frozenRecords.add(info);
	}
	
	public void removeFrozenRecord(FrozenAssetInfo info) {
		if(info == null){
			return;
		}
		if(this.frozenRecords == null){
			return;
		}
		FrozenAssetInfo theOne = null;
		for (FrozenAssetInfo recd : this.frozenRecords) {
			if(recd.getId().equals(info.getId())){
				theOne = recd;
				break;
			}
		}
		this.frozenRecords.remove(theOne);		
	}

	public long getUsableBalance() {
		long balance = getBalance();
		long frozenAmount = 0L;
		List<FrozenAssetInfo> infos = getFrozenRecords();
		if(infos != null){
			for (FrozenAssetInfo info : infos) {
				if(!info.isClosed()){
					frozenAmount += info.getBalance();
				}
			}
		}
		return balance-frozenAmount;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AssetInfo [id=" + id + ", type=" + type + ", accountId="
				+ getAccount().getId() + ", balance=" + balance 
				+ ", virtual=" + virtual + ", usableBalance=" + getUsableBalance() 
				+ ", createdBy=" + createdBy + ", lastUpdatedBy="
				+ lastUpdatedBy + ", createdDate=" + createdDate
				+ ", lastUpdatedDate=" + lastUpdatedDate + "]";
	}
	
	
	
}
