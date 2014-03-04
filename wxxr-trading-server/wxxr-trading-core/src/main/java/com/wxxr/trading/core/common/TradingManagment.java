/**
 * 
 */
package com.wxxr.trading.core.common;

import java.util.List;

import com.wxxr.common.microkernel.IKernelContext;
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
public abstract class TradingManagment implements ITradingManagment {

	private ITradingCodeManager tradingCodeManager;
	private IKernelContext context;
	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingManagment#submitTrading(com.wxxr.trading.core.model.ITrading)
	 */
	@Override
	public Long submitTrading(ITrading trading) throws TradingException {
		//trading ��Ϣ
		//��ʼ��trading code ��Ϣ
		ITradingCode tradingCode=getTradingCodeManager().buildITradingCode(trading);
		//�־û�trading code 
		Integer tradingCodeId=context.getService(ITradingCodeStorage.class).saveOrUpdate((TradingCodeObject)tradingCode);
		((TradingObject)trading).setTradingCode(tradingCodeId);
		//�־û�trading
		Long id=context.getService(ITradingObjectStorage.class).saveOrUpdate((TradingObject)trading);
		//����trading strategy �� Submit Process��process
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

	public void start(IKernelContext ctx) {
		this.context = ctx;
		
	}

	public void stop(IKernelContext ctx) {
		this.context = null;
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
