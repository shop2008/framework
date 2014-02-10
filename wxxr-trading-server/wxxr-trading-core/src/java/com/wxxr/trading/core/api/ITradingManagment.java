
package com.wxxr.trading.core.api;

import java.util.List;

import com.wxxr.trading.core.exception.TradingException;
import com.wxxr.trading.core.model.ITrading;
import com.wxxr.trading.core.model.ITradingTransaction;
import com.wxxr.trading.core.model.ITradingRecordFilter;
import com.wxxr.trading.core.model.ITradingRecord;
import com.wxxr.trading.core.model.TradingStatus;



/**
 * 交易服务管理者
 * @author wangyan
 *
 */
public interface ITradingManagment {
	
	Long submitTrading(ITrading trading)throws TradingException;;
	
	ITrading getTrading(Long tradingId);
	
	TradingStatus getTradingStatus(Long id) ;
	
	void cancelTrading(Long id) throws TradingException;;	
		
	List<ITradingTransaction> queryTradingOperations(Long tradingId);
	
	List<ITradingRecord> queryTradingDetails(Long tradingId) ;
	
	List<ITradingRecord> queryTradingDetails(Long tradingId,ITradingRecordFilter filter) ;
	
	List<ITradingRecord> queryOperationDetails(Long operationId);	
}
