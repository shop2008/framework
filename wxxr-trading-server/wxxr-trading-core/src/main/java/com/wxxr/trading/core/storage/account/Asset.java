/**
 * 
 */
package com.wxxr.trading.core.storage.account;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.smbiz.bizobject.AuditableLKeyObject;
import com.wxxr.trading.core.model.IAsset;
import com.wxxr.trading.core.model.IAssetFrozenItem;

/**
 * @author neillin
 *
 */
public abstract class Asset extends AuditableLKeyObject implements IAsset {

	private static final long serialVersionUID = -5759469583421599289L;
	private Long accountId;
	private long balance;	
	private boolean virtual;
	private Map<Long,IAssetFrozenItem> frozenRecords;
	private String type;
	
	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}
	/**
	 * @return the balance
	 */
	public long getBalance() {
		return balance;
	}
	/**
	 * @return the frozenAmount
	 */
	public long getFrozenAmount() {
		if((this.frozenRecords == null)||this.frozenRecords.isEmpty()){
			return 0L;
		}
		long val = 0;
		for (IAssetFrozenItem item : this.frozenRecords.values()) {
			val += item.getBalance();
		}
		return val;
	}
	
	public void addFrozenItem(IAssetFrozenItem item){
		if(item == null){
			throw new IllegalArgumentException("Frozen item is NULL !");
		}
		if(this.frozenRecords == null){
			this.frozenRecords = new HashMap<Long,IAssetFrozenItem>();
		}
		this.frozenRecords.put(item.getId(),item);
	}
	
//	public List<IAssetFrozenItem> getFrozenItems() {
//		List<IAssetFrozenItem> items = Collections.emptyList();
//		return this.frozenRecords != null && this.frozenRecords.size() > 0 ? Collections.unmodifiableList(this.frozenRecords) : items;
//	}
	
	/**
	 * @return the virtual
	 */
	public boolean isVirtual() {
		return virtual;
	}
	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(long balance) {
		this.balance = balance;
	}
	/**
	 * @param virtual the virtual to set
	 */
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AssetImpl [accountId=" + accountId + ", balance=" + balance
				+ ", frozenAmount=" + getFrozenAmount() + ", virtual=" + virtual
				+ ", CreatedBy=" + getCreatedBy() + ", createdDate="
				+ getCreatedDate() + ", lastUpdatedBy="
				+ getLastUpdatedBy() + ", lastUpdatedDate="
				+ getLastUpdatedDate() + ", Id =" + getId() + "]";
	}
	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.model.IAsset#getFrozenItem(java.lang.Long)
	 */
	@Override
	public IAssetFrozenItem getFrozenItem(Long itemId) {
		return this.frozenRecords != null ? this.frozenRecords.get(itemId) : null;
	}
	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.model.IAsset#getAllOpenFrozenItemIds()
	 */
	@Override
	public Long[] getAllOpenFrozenItemIds() {
		return this.frozenRecords != null && this.frozenRecords.size() > 0 ? this.frozenRecords.keySet().toArray(new Long[0]) : null;
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


}
