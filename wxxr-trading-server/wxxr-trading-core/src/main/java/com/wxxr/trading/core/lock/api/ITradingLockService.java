package com.wxxr.trading.core.lock.api;

import javax.transaction.Transaction;


/**
 * 锁服务
 * @author wangyan
 *
 */
public interface ITradingLockService {

	/**
	 * 创建锁
	 * @param lockType
	 * @param tx
	 * @return
	 */
	ITradingLock createTradingLock(Transaction tx);

}
