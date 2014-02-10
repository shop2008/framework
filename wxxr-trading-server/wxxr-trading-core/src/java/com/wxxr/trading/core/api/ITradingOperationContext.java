/**
 * 
 */
package com.wxxr.trading.core.api;

import com.wxxr.trading.core.model.ITrading;

/**
 * @author wangyan
 *
 */
public interface ITradingOperationContext {
	ITradingContext getTradingContext();
	ITrading getTrading();
}
