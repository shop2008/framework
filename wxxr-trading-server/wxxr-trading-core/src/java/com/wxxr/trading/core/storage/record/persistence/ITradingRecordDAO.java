/**
 * 
 */
package com.wxxr.trading.core.storage.record.persistence;

import com.wxxr.persistence.annotation.Command;
import com.wxxr.persistence.service.FindByPrimaryKeyCommand;
import com.wxxr.trading.core.storage.record.persistence.bean.TradingRecordInfo;

/**
 * @author neillin
 *
 */
public interface ITradingRecordDAO {
	Long add(TradingRecordInfo recd);
	
	@Command(clazz=FindByPrimaryKeyCommand.class)
	TradingRecordInfo findByPrimaryKey(Long id);

}
