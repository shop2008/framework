/**
 * 
 */
package com.wxxr.trading.core.api;

import com.wxxr.trading.core.exception.TradingException;
import com.wxxr.trading.core.model.ITradingTransaction;

/**
 * ITradingOperation Context
 * @author wangyan
 *
 */
public interface ITradingTransactionHandler<T extends ITradingTransaction> {
	void execute(ITradingTransactionContext transactionContext,T transaction) throws TradingException;
}
