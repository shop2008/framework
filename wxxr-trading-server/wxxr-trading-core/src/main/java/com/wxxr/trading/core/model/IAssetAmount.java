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
	 * 返回资产数额
	 * @return
	 */
	long getAmount();
	
	/**
	 * 返回资产类型
	 * @return
	 */
	String getAssetType();
	
	/**
	 * 如果是虚拟资产，返回true
	 * @return
	 */
	boolean isVirtual();
}
