/**
 * 
 */
package com.wxxr.trading.core.storage.account;

import com.wxxr.trading.core.exception.TradingException;
import com.wxxr.trading.core.model.IAssetAmount;

/**
 * @author neillin
 *
 */
public interface IAssetOperationHandler {
	void debit(Asset asset, IAssetAmount amount,String comments) throws TradingException;
	void credit(Asset asset, IAssetAmount amount,String comments) throws TradingException;
	void debitFromFrozen(AssetFrozenItem frozenItem, IAssetAmount amount,String comments) throws TradingException;
	Long freeze(Asset asset, IAssetAmount amount,String comments) throws TradingException;
	void unfrost(AssetFrozenItem frozenItem,String comments);
	Long newAsset(IAssetAmount amount,String comments) throws TradingException;
	
	void init(IAssetStorage.Context ctx);
	void destroy();

}
