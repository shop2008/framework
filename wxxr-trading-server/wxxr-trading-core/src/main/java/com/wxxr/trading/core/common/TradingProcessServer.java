/**
 * 
 */
package com.wxxr.trading.core.common;

import java.util.Date;

import com.wxxr.common.microkernel.IKernelContext;
import com.wxxr.trading.core.api.ITradingCodeManager;
import com.wxxr.trading.core.api.ITradingManagment;
import com.wxxr.trading.core.api.ITradingStrategy;
import com.wxxr.trading.core.api.ITradingTransactionContext;
import com.wxxr.trading.core.model.ITrading;
import com.wxxr.trading.core.storage.transaction.AbstractTransaction;
import com.wxxr.trading.core.storage.transaction.ITransactionStorage;
import com.wxxr.trading.core.task.api.ISchedulerContext;
import com.wxxr.trading.core.task.api.ITaskExecutor;
import com.wxxr.trading.core.task.api.ITaskScheduler;

/**
 * @author wangyan
 *
 */
public abstract class  TradingProcessServer {
	private IKernelContext context;
	
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
	
	public void start(IKernelContext ctx) {
		this.context = ctx;
	}

	public void stop(IKernelContext ctx) {
		this.context = null;
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
		public void notifyTransactionSuccess(Long transactionId){
			context.getService(ITaskScheduler.class).scheduleTask("T_TRANSACTION_EVENT", transactionId+"", new Date());
		}
	}
}
