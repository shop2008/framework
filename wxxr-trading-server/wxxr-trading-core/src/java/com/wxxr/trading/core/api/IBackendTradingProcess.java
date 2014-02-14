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
public interface IBackendTradingProcess<T extends ITrading> extends ITradingProcess {
	void cancelTrading(T trading) throws TradingException;
	void handleTransactionSucceed(T trading,ITradingTransaction transaction);
	void handleTransactionFailed(T trading,ITradingTransaction transaction);
}
