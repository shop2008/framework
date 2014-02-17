/**
 * 
 */
package com.wxxr.trading.core.common;

import com.wxxr.common.jmx.annotation.ServiceMBean;
import com.wxxr.stock.common.service.AbstractModule;
import com.wxxr.stock.common.service.api.IMobileStockAppContext;
import com.wxxr.stock.common.service.api.ISchedulerContext;
import com.wxxr.stock.common.service.api.ITaskExecutor;
import com.wxxr.trading.core.api.ITradingCodeManager;
import com.wxxr.trading.core.api.ITradingContext;
import com.wxxr.trading.core.api.ITradingManagment;
import com.wxxr.trading.core.api.ITradingStrategy;
import com.wxxr.trading.core.api.ITradingTransactionContext;
import com.wxxr.trading.core.model.ITrading;
import com.wxxr.trading.core.model.ITradingCode;
import com.wxxr.trading.core.storage.transaction.AbstractTransaction;
import com.wxxr.trading.core.storage.transaction.ITransactionStorage;

/**
 * @author wangyan
 *
 */
@ServiceMBean
public class TradingProcessServer extends AbstractModule{
	private ITaskExecutor transactionEventQueryExecutor = new ITaskExecutor(){
		@Override
		public void executeTask(ISchedulerContext ctx, Long jobId, String jobRequest)
				throws Exception {
			
		}
		@Override
		public String getAcceptableTaskType() {
			return "T_TRANSACTION_EVENT";
		}
	};
	
	private ITaskExecutor tradingBackendExecutor=new ITaskExecutor() {
		@Override
		public void executeTask(ISchedulerContext ctx, Long jobId, String jobRequest)
				throws Exception {
			
		}
		@Override
		public String getAcceptableTaskType() {
			return "T_TRADING_BACKEND";
		}
	};
	private ITaskExecutor cancelTradingExecutor=new ITaskExecutor() {
		
		@Override
		public void executeTask(ISchedulerContext ctx, Long jobId, String jobRequest)
				throws Exception {
			
		}
		@Override
		public String getAcceptableTaskType() {
			return "T_CANCEL_TRADING";
		}
		
	};
	protected void processTrading(Long tradingId){
		ITrading trading=context.getService(ITradingManagment.class).getTrading(tradingId);
		ITradingStrategy<ITrading> tradingStrategy=context.getService(ITradingCodeManager.class).getStrategy(trading.getType());
		TradingTransactionContext transactionContext=new TradingTransactionContext();
		
		tradingStrategy.getBackendProcess().start(transactionContext);
	}
	protected void processTransaction(Long transactionId) {
		AbstractTransaction transaction=context.getService(ITransactionStorage.class).get(transactionId);
		ITrading trading=context.getService(ITradingManagment.class).getTrading(transaction.getTradingId());
		ITradingStrategy<ITrading> tradingStrategy=context.getService(ITradingCodeManager.class).getStrategy(trading.getType());
		TradingTransactionContext transactionContext=new TradingTransactionContext();
		tradingStrategy.getBackendProcess().handleTransaction(transactionContext, transaction);
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.stock.common.service.AbstractModule#registerService(com.wxxr.stock.common.service.api.IMobileStockAppContext)
	 */
	@Override
	protected void registerService(IMobileStockAppContext ctx) {
		
	}
	/* (non-Javadoc)
	 * @see com.wxxr.stock.common.service.AbstractModule#unregisterService(com.wxxr.stock.common.service.api.IMobileStockAppContext)
	 */
	@Override
	protected void unregisterService(IMobileStockAppContext ctx) {
		
	}
	class TradingTransactionContext implements ITradingTransactionContext{
		private ITrading trading;
		/* (non-Javadoc)
		 * @see com.wxxr.trading.core.api.ITradingTransactionContext#getTrading()
		 */
		@Override
		public ITrading getTrading() {
			return trading;
		}
		/**
		 * @param trading the trading to set
		 */
		public void setTrading(ITrading trading) {
			this.trading = trading;
		}
		
	}
}
