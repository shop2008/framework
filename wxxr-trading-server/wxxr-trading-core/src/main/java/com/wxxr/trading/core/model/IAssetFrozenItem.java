/**
 * 
 */
package com.wxxr.trading.core.model;

import com.wxxr.smbiz.bizobject.IAuditableLKeyObject;

/**
 * @author neillin
 *
 */
public interface IAssetFrozenItem extends IAuditableLKeyObject{
	Long getAssetId();
	long getBalance();
	long getTotalAmount();
	String getComments();
}
