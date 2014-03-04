/**
 * 
 */
package com.wxxr.trading.core.api;

import com.wxxr.trading.core.exception.TradingException;
import com.wxxr.trading.core.model.ITradingTransaction;


/**
 * @author wangyan
 *
 */
public interface ITradingTransactionService {
	void processTransaction(ITradingTransactionContext transactionContext,ITradingTransaction tradingTransaction)throws TradingException;
	void registerTransactionHandler(String opCode, ITradingTransactionHandler<ITradingTransaction> handler);
	void unregisterTransactionHandler(String opCode, ITradingTransactionHandler<ITradingTransaction> handler);
	
}
