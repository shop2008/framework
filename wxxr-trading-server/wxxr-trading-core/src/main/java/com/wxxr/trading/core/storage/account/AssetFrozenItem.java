/**
 * 
 */
package com.wxxr.trading.core.storage.account;

import com.wxxr.smbiz.bizobject.AuditableLKeyObject;
import com.wxxr.trading.core.model.IAssetFrozenItem;

/**
 * @author neillin
 *
 */
public class AssetFrozenItem extends AuditableLKeyObject implements
		IAssetFrozenItem {
	private static final long serialVersionUID = 4042127741488911233L;

	private Long assetId;
	
	private long balance;
	
	private long totalAmount;
	
	private String comments;
	
	private boolean closed;

	/**
	 * @return the assetId
	 */
	public Long getAssetId() {
		return assetId;
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
	 * @param assetId the assetId to set
	 */
	public void setAssetId(Long assetId) {
		this.assetId = assetId;
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
		return "AssetFrozenRecord [assetId=" + assetId + ", balance=" + balance
				+ ", totalAmount=" + totalAmount + ", comments=" + comments
				+ ", closed=" + closed + ", getCreatedBy()=" + getCreatedBy()
				+ ", getCreatedDate()=" + getCreatedDate()
				+ ", getLastUpdatedBy()=" + getLastUpdatedBy()
				+ ", getLastUpdatedDate()=" + getLastUpdatedDate()
				+ ", getId()=" + getId() + "]";
	}


}
