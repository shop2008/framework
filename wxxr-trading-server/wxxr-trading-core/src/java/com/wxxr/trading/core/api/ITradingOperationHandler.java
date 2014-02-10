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
public interface ITradingOperationHandler<T extends ITradingTransaction> {
	void execute(ITradingOperationContext operationContext,T operation) throws TradingException;
}
