/**
 * 
 */
package com.wxxr.trading.core.storage.tradingcode.persistence;

import com.wxxr.trading.core.storage.tradingcode.persistence.bean.TradingCodeInfo;

/**
 * @author neillin
 *
 */
public interface ITradingCodeDAO {
	TradingCodeInfo findByPrimaryKey(Integer key);
	Integer add(TradingCodeInfo info);
	void update(TradingCodeInfo info);
}
