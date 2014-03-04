/**
 * 
 */
package com.wxxr.trading.core.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.wxxr.common.jmx.annotation.ServiceMBean;
import com.wxxr.common.microkernel.AbstractModule;
import com.wxxr.common.microkernel.IKernelContext;
import com.wxxr.common.microkernel.IMicroKernal;
import com.wxxr.trading.core.api.ITradingTransactionContext;
import com.wxxr.trading.core.api.ITradingTransactionHandler;
import com.wxxr.trading.core.api.ITradingTransactionService;
import com.wxxr.trading.core.exception.TradingException;
import com.wxxr.trading.core.model.ITradingTransaction;
import com.wxxr.trading.core.storage.account.TxStatus;
import com.wxxr.trading.core.storage.transaction.AbstractTransaction;
import com.wxxr.trading.core.storage.transaction.ITransactionStorage;

/**
 * @author wangyan
 *
 */
public abstract class TradingTransactionService implements ITradingTransactionService {
	
	private IKernelContext context;

	private Map<String, ITradingTransactionHandler<ITradingTransaction>> handlers = new ConcurrentHashMap<String, ITradingTransactionHandler<ITradingTransaction>>();

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingTransactionService#processOperation(com.wxxr.trading.core.api.ITradingTransactionContext, com.wxxr.trading.core.model.ITradingTransaction)
	 */
	@Override
	public void processTransaction(ITradingTransactionContext transactionContext,
			ITradingTransaction tradingTransaction) throws TradingException {
		Long transactionId=context.getService(ITransactionStorage.class).saveOrUpdate((AbstractTransaction)tradingTransaction);
		ITradingTransactionHandler<ITradingTransaction> handler=handlers.get(tradingTransaction.getTransactionCode());
		handler.execute(transactionContext, tradingTransaction);
		AbstractTransaction transaction=context.getService(ITransactionStorage.class).get(transactionId);
		transaction.setStatus(TxStatus.DONE);
		transactionContext.notifyTransactionSuccess(transactionId);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingTransactionService#registerOperationHandler(java.lang.String, com.wxxr.trading.core.api.ITradingOperationHandler)
	 */
	@Override
	public void registerTransactionHandler(String opCode,
			ITradingTransactionHandler<ITradingTransaction> handler) {
		handlers.put(opCode, handler);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingTransactionService#unregisterOperationHandler(java.lang.String, com.wxxr.trading.core.api.ITradingOperationHandler)
	 */
	@Override
	public void unregisterTransactionHandler(String opCode,
			ITradingTransactionHandler<ITradingTransaction> handler) {
		ITradingTransactionHandler<? extends ITradingTransaction> tradingTransactionHandler=handlers.get(opCode);
		if(tradingTransactionHandler==handler){
			handlers.remove(handler);
		}
	}

	public void start(IKernelContext ctx) {
		this.context = ctx;
		ctx.registerService(ITradingTransactionService.class, this);
	}

	public void stop(IKernelContext ctx) {
		ctx.unregisterService(ITradingTransactionService.class, this);
		this.context = null;
	}
	

}
