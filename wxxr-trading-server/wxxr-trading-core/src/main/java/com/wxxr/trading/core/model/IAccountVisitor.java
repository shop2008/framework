/**
 * 
 */
package com.wxxr.trading.core.model;

/**
 * @author neillin
 *
 */
public interface IAccountVisitor {
	/**
	 * 如果不需要访问下一个账户，返回false， 返回true则继续访问下一个账户
	 * @param account
	 * @return
	 */
	boolean doVisit(IAssetAccount account);
}
