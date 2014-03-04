package com.wxxr.trading.core.lock.api;

import javax.transaction.Transaction;


/**
 * ������
 * @author wangyan
 *
 */
public interface ITradingLockService {

	/**
	 * ������
	 * @param lockType
	 * @param tx
	 * @return
	 */
	ITradingLock createTradingLock(Transaction tx);

}
