package com.wxxr.trading.core.storage.account;

import com.wxxr.trading.core.model.ITradingRecord;
import com.wxxr.trading.core.storage.account.bean.AssetInfo;
import com.wxxr.trading.core.storage.api.IBizObjectStorage;

public interface IAssetStorage extends IBizObjectStorage<Long, Asset, AssetInfo> {
	
	public interface Context {
		IAssetStorage getStorage();
		void saveTradingRecord(ITradingRecord record);
	}
	
	void updateFrozenItem(AssetFrozenItem item);
	Long addFrozenItem(AssetFrozenItem item);
	AssetFrozenItem findFrozenItem(Long itemId);
	
	IAssetOperationHandler getAssetOperationHandler(String assetType);	
	void registerAssetOperationHandler(String assetType, IAssetOperationHandler handler);
	boolean unregisterAssetOperationHandler(String assetType, IAssetOperationHandler handler);
}
