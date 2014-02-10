/**
 * 
 */
package com.wxxr.trading.core.storage.trading.persistence;

import com.wxxr.trading.core.storage.trading.bean.TradingInfo;


/**
 * @author neillin
 *
 */
public interface ITradingInfoDAO {
	TradingInfo findByPrimaryKey(Long key);
	Long add(TradingInfo info);
	void update(TradingInfo info);
}
