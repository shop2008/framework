/**
 * 
 */
package com.wxxr.trading.core.storage.trading.persistence;

import com.wxxr.trading.core.storage.trading.bean.TradingPartyInfo;

/**
 * @author neillin
 *
 */
public interface ITradingPartyDAO {
	TradingPartyInfo findByPrimaryKey(Long key);
	Long add(TradingPartyInfo info);
	void update(TradingPartyInfo info);
}
