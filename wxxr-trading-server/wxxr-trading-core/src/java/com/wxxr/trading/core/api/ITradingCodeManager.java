/**
 * 
 */
package com.wxxr.trading.core.api;

import com.wxxr.trading.core.model.ITrading;
import com.wxxr.trading.core.model.ITradingCode;

/**
 * @author neillin
 *
 */
public interface ITradingCodeManager {
	void registerStrategy(String tradingCode,ITradingStrategy<? extends ITrading> strategy);
	void unregisterStrategy(String tradingCode,ITradingStrategy<? extends ITrading> strategy);
	ITradingCode getTradingCode(String tradingCode);
	ITradingCode getTradingCode(Integer tradingCodeId);
	<T extends ITrading> ITradingStrategy<T> getStrategy(String tradingCode);
	
}
