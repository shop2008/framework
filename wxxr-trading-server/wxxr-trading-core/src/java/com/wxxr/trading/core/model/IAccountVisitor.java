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
	 * �������Ҫ������һ���˻�������false�� ����true�����������һ���˻�
	 * @param account
	 * @return
	 */
	boolean doVisit(IAssetAccount account);
}
