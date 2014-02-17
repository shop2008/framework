/**
 * 
 */
package com.wxxr.trading.core.common;

import java.util.List;

import com.wxxr.common.jmx.annotation.ServiceMBean;
import com.wxxr.stock.common.service.AbstractModule;
import com.wxxr.stock.common.service.api.IMobileStockAppContext;
import com.wxxr.trading.core.api.ITradingCodeManager;
import com.wxxr.trading.core.api.ITradingContext;
import com.wxxr.trading.core.api.ITradingManagment;
import com.wxxr.trading.core.exception.TradingException;
import com.wxxr.trading.core.model.ITrading;
import com.wxxr.trading.core.model.ITradingCode;
import com.wxxr.trading.core.model.ITradingRecord;
import com.wxxr.trading.core.model.ITradingRecordFilter;
import com.wxxr.trading.core.model.ITradingTransaction;
import com.wxxr.trading.core.model.TradingStatus;
import com.wxxr.trading.core.storage.trading.ITradingObjectStorage;
import com.wxxr.trading.core.storage.trading.TradingObject;
import com.wxxr.trading.core.storage.tradingcode.ITradingCodeStorage;
import com.wxxr.trading.core.storage.tradingcode.TradingCodeObject;

/**
 * @author wangyan
 *
 */
@ServiceMBean
public class TradingManagment extends AbstractModule implements ITradingManagment {

	private ITradingCodeManager tradingCodeManager;
	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingManagment#submitTrading(com.wxxr.trading.core.model.ITrading)
	 */
	@Override
	public Long submitTrading(ITrading trading) throws TradingException {
		//trading 信息
		//初始化trading code 信息
		ITradingCode tradingCode=getTradingCodeManager().buildITradingCode(trading);
		//持久化trading code 
		Integer tradingCodeId=context.getService(ITradingCodeStorage.class).saveOrUpdate((TradingCodeObject)tradingCode);
		((TradingObject)trading).setTradingCode(tradingCodeId);
		//持久化trading
		Long id=context.getService(ITradingObjectStorage.class).saveOrUpdate((TradingObject)trading);
		//调用trading strategy 的 Submit Process的process
		TradingContext tradingContext=new TradingContext();
		tradingContext.setTradingCode(tradingCode);
		getTradingCodeManager().getStrategy(trading.getType()).getSubmitProcess().process(tradingContext, trading);
		return id;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingManagment#getTrading(java.lang.Long)
	 */
	@Override
	public ITrading getTrading(Long tradingId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingManagment#getTradingStatus(java.lang.Long)
	 */
	@Override
	public TradingStatus getTradingStatus(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingManagment#cancelTrading(java.lang.Long)
	 */
	@Override
	public void cancelTrading(Long id) throws TradingException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingManagment#queryTradingOperations(java.lang.Long)
	 */
	@Override
	public List<ITradingTransaction> queryTradingOperations(Long tradingId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingManagment#queryTradingDetails(java.lang.Long)
	 */
	@Override
	public List<ITradingRecord> queryTradingDetails(Long tradingId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingManagment#queryTradingDetails(java.lang.Long, com.wxxr.trading.core.model.ITradingRecordFilter)
	 */
	@Override
	public List<ITradingRecord> queryTradingDetails(Long tradingId,
			ITradingRecordFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingManagment#queryOperationDetails(java.lang.Long)
	 */
	@Override
	public List<ITradingRecord> queryOperationDetails(Long operationId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.common.service.AbstractModule#registerService(com.wxxr.stock.common.service.api.IMobileStockAppContext)
	 */
	@Override
	protected void registerService(IMobileStockAppContext arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.common.service.AbstractModule#unregisterService(com.wxxr.stock.common.service.api.IMobileStockAppContext)
	 */
	@Override
	protected void unregisterService(IMobileStockAppContext arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the tradingCodeManager
	 */
	public ITradingCodeManager getTradingCodeManager() {
		if(tradingCodeManager==null){
			tradingCodeManager=context.getService(ITradingCodeManager.class);
		}
		return tradingCodeManager;
	}

	
	class TradingContext implements ITradingContext{
		private ITradingCode tradingCode;
		/* (non-Javadoc)
		 * @see com.wxxr.trading.core.api.ITradingContext#getTradingCode()
		 */
		@Override
		public ITradingCode getTradingCode() {
			return tradingCode;
		}
		/**
		 * @param tradingCode the tradingCode to set
		 */
		public void setTradingCode(ITradingCode tradingCode) {
			this.tradingCode = tradingCode;
		}
		
	}
}
