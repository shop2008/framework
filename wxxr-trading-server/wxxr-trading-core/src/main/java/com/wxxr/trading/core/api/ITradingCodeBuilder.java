/**
 * 
 */
package com.wxxr.trading.core.api;

import com.wxxr.trading.core.model.ITrading;
import com.wxxr.trading.core.model.ITradingCode;


/**
 * @author wangyan
 *
 */
public interface ITradingCodeBuilder {
	ITradingCode build(ITrading trading);
	String getSupportType();
}
