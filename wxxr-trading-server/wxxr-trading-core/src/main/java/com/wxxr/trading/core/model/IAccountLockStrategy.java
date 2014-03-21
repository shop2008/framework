/**
 * 
 */
package com.wxxr.trading.core.model;

import java.util.concurrent.TimeUnit;

/**
 * ʵ���˻�ȫ��������
 * @author neillin
 *
 */
public interface IAccountLockStrategy {
	
	/**
	 * ���ָ���˻������������ָ����ʱ���ڳɹ������������true�����򷵻�false
	 * @param acctId
	 * @param timeout
	 * @param unit
	 * @return
	 */
	boolean lockAccount(String acctId, long timeout, TimeUnit unit);
	
	/**
	 * �Ƴ�ָ�����˻���������˻����Ѿ����Ƴ����Ѿ����ڣ�����false
	 * @param acctId
	 * @return
	 */
	boolean unlockAccount(String acctId);
	
	
}
