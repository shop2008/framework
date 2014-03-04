/**
 * 
 */
package com.wxxr.trading.core.model;

import java.util.List;

import com.wxxr.smbiz.bizobject.IAuditableLKeyObject;
import com.wxxr.trading.core.storage.api.InheritableBizObject;

/**
 * @author neillin
 *
 */
public interface IAssetAccount extends InheritableBizObject<Long>,IAuditableLKeyObject {
	String getOwnerId();
	List<Long> getAllAssets();
	IAsset getAsset(Long assetId);
}
