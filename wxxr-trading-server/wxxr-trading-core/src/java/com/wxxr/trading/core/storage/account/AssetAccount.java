/**
 * 
 */
package com.wxxr.trading.core.storage.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wxxr.smbiz.bizobject.AuditableLKeyObject;
import com.wxxr.trading.core.model.IAssetAccount;

/**
 * @author neillin
 *
 */
public abstract class AssetAccount extends AuditableLKeyObject implements
		IAssetAccount {

	private static final long serialVersionUID = 643155059353587789L;
	private String ownerId;
	private List<Asset> assets;
	private String type;
	
	/* (non-Javadoc)
	 * @see com.wxxr.stock.trading.account.model.AssetAccount#getOwnerId()
	 */
	@Override
	public String getOwnerId() {
		return this.ownerId;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.trading.account.model.AssetAccount#getAllAssets()
	 */
	@Override
	public List<Long> getAllAssets() {
		List<Long> list = Collections.emptyList();
		if((this.assets != null)&&(this.assets.size() > 0)){
			list = new ArrayList<Long>();
			for (Asset asset : this.assets) {
				list.add(asset.getId());
			}
		}
		return list;
	}


	/* (non-Javadoc)
	 * @see com.wxxr.stock.trading.account.model.AssetAccount#getAsset(java.lang.Long)
	 */
	@Override
	public Asset getAsset(Long assetId) {
		if((this.assets != null)&&(this.assets.size() > 0)){
			for (Asset asset : this.assets) {
				if(asset.getId().equals(assetId)){
					return asset;
				}
			}
		}
		return null;
	}

	public void addAsset(Asset asset) {
		if(this.assets == null){
			this.assets = new ArrayList<Asset>();
		}
		Asset old = getAsset(asset.getId());
		if(old != null){
			this.assets.remove(old);
		}
		this.assets.add(asset);
	}

	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AssetAccountImpl [ownerId=" + ownerId
				+ ", CreatedBy=" + getCreatedBy() + ", createdDate="
				+ getCreatedDate() + ", lastUpdatedBy="
				+ getLastUpdatedBy() + ", lastUpdatedDate="
				+ getLastUpdatedDate() + ", Id =" + getId() + "]";
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
