/**
 * 
 */
package com.wxxr.trading.core.api;

import com.wxxr.trading.core.model.ITrading;

/**
 * ½»Ò×²ßÂÔ
 * @author wangyan
 *
 */
public interface ITradingStrategy<T extends ITrading> {
	ITradingProcess<T> getSubmitProcess();
	ITradingProcess<T> getBackendProcess();
}
