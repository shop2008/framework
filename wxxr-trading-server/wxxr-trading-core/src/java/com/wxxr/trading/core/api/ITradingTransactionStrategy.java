/**
 * 
 */
package com.wxxr.trading.core.api;

import com.wxxr.trading.core.model.ITrading;
import com.wxxr.trading.core.model.ITradingTransaction;

/**
 * @author wangyan
 *
 */
public interface ITradingTransactionStrategy<T extends ITrading> {
	ITradingTransaction getBeginTradingTransaction(ITradingContext context, T trading);
	ITradingTransaction[] getNextTranscations(ITradingContext context, T trading,ITradingTransaction transaction);
}
