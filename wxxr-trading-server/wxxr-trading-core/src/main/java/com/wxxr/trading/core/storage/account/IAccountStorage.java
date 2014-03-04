/**
 * 
 */
package com.wxxr.trading.core.storage.account;

import com.wxxr.trading.core.model.ITradingRecord;
import com.wxxr.trading.core.storage.account.bean.AssetAccountInfo;
import com.wxxr.trading.core.storage.api.IBizObjectStorage;

/**
 * @author neillin
 *
 */
public interface IAccountStorage extends IBizObjectStorage<Long,AssetAccount, AssetAccountInfo> {
	public interface Context {
		IAccountStorage getStorage();
		void saveTradingRecord(ITradingRecord record);
	}

	IAccountOperationHandler getAccountOperationHandler(String acctType);	
	void registerAccountOperationHandler(String acctType, IAccountOperationHandler handler);
	boolean unregisterAccountOperationHandler(String acctType, IAccountOperationHandler handler);

}
