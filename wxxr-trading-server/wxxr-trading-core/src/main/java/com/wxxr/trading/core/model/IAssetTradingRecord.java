/**
 * 
 */
package com.wxxr.trading.core.model;

/**
 * �ʲ����׼�¼
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
