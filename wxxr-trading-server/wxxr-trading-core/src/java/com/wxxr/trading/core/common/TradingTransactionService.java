/**
 * 
 */
package com.wxxr.trading.core.common;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.wxxr.common.jmx.annotation.ServiceMBean;
import com.wxxr.stock.common.service.AbstractModule;
import com.wxxr.stock.common.service.api.IMobileStockAppContext;
import com.wxxr.stock.common.service.api.ITaskScheduler;
import com.wxxr.trading.core.api.ITradingTransactionContext;
import com.wxxr.trading.core.api.ITradingTransactionHandler;
import com.wxxr.trading.core.api.ITradingTransactionService;
import com.wxxr.trading.core.exception.TradingException;
import com.wxxr.trading.core.model.ITradingTransaction;
import com.wxxr.trading.core.storage.transaction.AbstractTransaction;
import com.wxxr.trading.core.storage.transaction.ITransactionStorage;

/**
 * @author wangyan
 *
 */
@ServiceMBean
public class TradingTransactionService  extends AbstractModule implements ITradingTransactionService {

	private Map<String, ITradingTransactionHandler<ITradingTransaction>> handlers = new ConcurrentHashMap<String, ITradingTransactionHandler<ITradingTransaction>>();

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingTransactionService#processOperation(com.wxxr.trading.core.api.ITradingTransactionContext, com.wxxr.trading.core.model.ITradingTransaction)
	 */
	@Override
	public void processTransaction(ITradingTransactionContext transactionContext,
			ITradingTransaction tradingTransaction) throws TradingException {
		Long id=context.getService(ITransactionStorage.class).saveOrUpdate((AbstractTransaction)tradingTransaction);
		ITradingTransactionHandler<ITradingTransaction> handler=handlers.get(tradingTransaction.getTransactionCode());
		handler.execute(transactionContext, tradingTransaction);
		notifyTransactionSuccess(id);
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

	/* (non-Javadoc)
	 * @see com.wxxr.stock.common.service.AbstractModule#registerService(com.wxxr.stock.common.service.api.IMobileStockAppContext)
	 */
	@Override
	protected void registerService(IMobileStockAppContext ctx) {
		ctx.registerService(ITradingTransactionService.class, this);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.common.service.AbstractModule#unregisterService(com.wxxr.stock.common.service.api.IMobileStockAppContext)
	 */
	@Override
	protected void unregisterService(IMobileStockAppContext ctx) {
		ctx.unregisterService(ITradingTransactionService.class, this);
	}
	protected void notifyTransactionSuccess(Long transactionId){
		context.getService(ITaskScheduler.class).scheduleTask("T_TRANSACTION_SUCCESS", transactionId+"", new Date());
	}

}
