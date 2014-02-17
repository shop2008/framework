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
	void registerStrategy(String tradingCode,ITradingStrategy<ITrading> strategy);
	void unregisterStrategy(String tradingCode,ITradingStrategy<ITrading> strategy);
	ITradingCode getTradingCode(String tradingCode);
	ITradingCode getTradingCode(Integer tradingCodeId);
	ITradingStrategy<ITrading> getStrategy(String tradingCode);
	ITradingCode buildITradingCode(ITrading trading);
}
