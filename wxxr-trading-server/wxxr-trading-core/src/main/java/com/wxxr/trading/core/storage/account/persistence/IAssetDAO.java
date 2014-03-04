/**
 * 
 */
package com.wxxr.trading.core.storage.account.persistence;

import com.wxxr.persistence.annotation.Command;
import com.wxxr.persistence.service.FindByPrimaryKeyCommand;
import com.wxxr.trading.core.storage.account.bean.AssetInfo;

/**
 * @author neillin
 *
 */
public interface IAssetDAO {
	Long add(AssetInfo asset);
	void update(AssetInfo asset);
	void remove(AssetInfo asset);
	
	@Command(clazz=FindByPrimaryKeyCommand.class)
	AssetInfo findByPrimaryKey(Long id);
	
//	@Command(clazz=FindAllAssetsByAccountIdCommand.class)
//	Collection<AssetInfo> findAllAssetsByAccountId(Long acctId);
//	
//	@Command(clazz=FindAssetByAccountIdNTypeCommand.class)
//	Collection<AssetInfo> findAssetByAccountIdNType(Long acctId,AssetType type, boolean virtual,IAccountAssetInfoFilter filter);
	
}
