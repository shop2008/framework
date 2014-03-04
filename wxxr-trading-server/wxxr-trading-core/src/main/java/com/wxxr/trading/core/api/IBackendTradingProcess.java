/**
 * 
 */
package com.wxxr.trading.core.api;


import com.wxxr.trading.core.exception.TradingException;
import com.wxxr.trading.core.model.ITrading;
import com.wxxr.trading.core.model.ITradingTransaction;

/**
 * @author wangyan
 *
 */
public interface IBackendTradingProcess<T extends ITrading> {
	void start(ITradingTransactionContext context);
	void cancelTrading(T trading) throws TradingException;
	void handleTransaction(ITradingTransactionContext context,ITradingTransaction transaction);
}
