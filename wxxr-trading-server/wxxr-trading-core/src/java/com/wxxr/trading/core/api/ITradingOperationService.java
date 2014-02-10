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
public interface ITradingOperationService {
	void processOperation(ITradingOperationContext tradingContext,ITradingTransaction operation)throws TradingException;
	void registerOperationHandler(String opCode, ITradingOperationHandler<? extends ITradingTransaction> handler);
	boolean unregisterOperationHandler(String opCode, ITradingOperationHandler<? extends ITradingTransaction> handler);
	
}
