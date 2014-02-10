/**
 * 
 */
package com.wxxr.trading.core.storage.account.persistence;

import com.wxxr.persistence.annotation.Command;
import com.wxxr.persistence.service.FindByPrimaryKeyCommand;
import com.wxxr.trading.core.storage.account.bean.FrozenAssetInfo;


/**
 * @author neillin
 *
 */
public interface IFrozenAssetDAO {
	Long add(FrozenAssetInfo asset);
	void update(FrozenAssetInfo asset);
	
	@Command(clazz=FindByPrimaryKeyCommand.class)
	FrozenAssetInfo findByPrimaryKey(Long id);

}
