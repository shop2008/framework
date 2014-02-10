/**
 * 
 */
package com.wxxr.trading.core.api;

import com.wxxr.trading.core.exception.TradingException;
import com.wxxr.trading.core.model.ITrading;

/**
 * @author neillin
 *
 */
public interface ITradingProcess<T extends ITrading> {
	void process(ITradingContext context, T trading) throws TradingException;
}
