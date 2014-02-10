/**
 * 
 */
package com.wxxr.trading.core.model;

import java.util.concurrent.TimeUnit;

/**
 * 实现账户全局锁策略
 * @author neillin
 *
 */
public interface IAccountLockStrategy {
	
	/**
	 * 获得指定账户的锁，如果在指定的时间内成功获得锁，返回true，否则返回false
	 * @param acctId
	 * @param timeout
	 * @param unit
	 * @return
	 */
	boolean lockAccount(String acctId, long timeout, TimeUnit unit);
	
	/**
	 * 移除指定的账户锁，如果账户锁已经被移除或已经过期，返回false
	 * @param acctId
	 * @return
	 */
	boolean unlockAccount(String acctId);
	
	
}
