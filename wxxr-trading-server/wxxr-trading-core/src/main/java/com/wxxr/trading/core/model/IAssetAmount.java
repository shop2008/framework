/**
 * 
 */
package com.wxxr.trading.core.model;

/**
 * @author neillin
 *
 */
public interface IAssetAmount {
	/**
	 * �����ʲ�����
	 * @return
	 */
	long getAmount();
	
	/**
	 * �����ʲ�����
	 * @return
	 */
	String getAssetType();
	
	/**
	 * ����������ʲ�������true
	 * @return
	 */
	boolean isVirtual();
}
