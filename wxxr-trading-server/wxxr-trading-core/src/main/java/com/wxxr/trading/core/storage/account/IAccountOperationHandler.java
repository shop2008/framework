/**
 * 
 */
package com.wxxr.trading.core.storage.account;

import java.util.Map;

import com.wxxr.trading.core.model.IAssetAccount;


/**
 * @author neillin
 *
 */
public interface IAccountOperationHandler {
	Long openAccount(String ownerId,String acctType,Map<String, Object> params);
	void closeAccount(IAssetAccount account);
	
	void init(IAccountStorage.Context ctx);
	void destroy();

}
