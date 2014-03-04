/**
 * 
 */
package com.wxxr.trading.core.model;

import com.wxxr.smbiz.bizobject.IAuditableLKeyObject;
import com.wxxr.trading.core.storage.api.InheritableBizObject;

/**
 * @author neillin
 *
 */
public interface IAsset extends InheritableBizObject<Long>,IAuditableLKeyObject{
	/**
	 * 所属资产账户
	 * @return
	 */
	Long getAccountId();
	
	/**
	 * 资产余额
	 * @return
	 */
	long getBalance();
	
	/**
	 * 冻结资产余额
	 * @return
	 */
	long getFrozenAmount();
	
	/**
	 * 是否虚拟资产
	 * @return
	 */
	boolean isVirtual();
	
	/**
	 * 返回资产总额
	 * @return
	 */
	long getAssetTotalAmount();
	
	IAssetFrozenItem getFrozenItem(Long itemId);
	
	Long[] getAllOpenFrozenItemIds();
	
	/**
	 * 创建指定资产额度的对象
	 * @param value
	 * @return
	 */
	IAssetAmount createAssetAmount(long value);
	
	
	boolean isMyAmount(IAssetAmount amount);
	
}
