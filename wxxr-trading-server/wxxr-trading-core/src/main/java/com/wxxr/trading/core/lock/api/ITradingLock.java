package com.wxxr.trading.core.lock.api;

import java.util.concurrent.TimeUnit;

import com.wxxr.trading.core.exception.LockFailedException;

/**
 * 
 * @author wangyan
 *
 */
public interface ITradingLock {

	/**
	 * Ϊid����
	 * @param id
	 * @throws LockFailedException
	 */
	public void lock(String id, long timeout, TimeUnit unit) throws LockFailedException ;
	
	/**
	 * �ͷ���
	 */
	public void destory();
}
