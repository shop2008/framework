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
	 * �����ʲ��˻�
	 * @return
	 */
	Long getAccountId();
	
	/**
	 * �ʲ����
	 * @return
	 */
	long getBalance();
	
	/**
	 * �����ʲ����
	 * @return
	 */
	long getFrozenAmount();
	
	/**
	 * �Ƿ������ʲ�
	 * @return
	 */
	boolean isVirtual();
	
	/**
	 * �����ʲ��ܶ�
	 * @return
	 */
	long getAssetTotalAmount();
	
	IAssetFrozenItem getFrozenItem(Long itemId);
	
	Long[] getAllOpenFrozenItemIds();
	
	/**
	 * ����ָ���ʲ���ȵĶ���
	 * @param value
	 * @return
	 */
	IAssetAmount createAssetAmount(long value);
	
	
	boolean isMyAmount(IAssetAmount amount);
	
}
