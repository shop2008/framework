/**
 * 
 */
package com.wxxr.trading.core.model;

/**
 * 资产交易记录
 * @author neillin
 *
 */
public interface IAssetTradingRecord extends ITradingRecord {
	Long getAssetId();
	long getAmount();
	long getOrigBalance();
	long getNewBalance();
	Long getFrozenId();
	long getUsableBalance();
}
