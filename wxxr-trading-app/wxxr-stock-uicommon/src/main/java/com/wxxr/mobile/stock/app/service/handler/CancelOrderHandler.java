package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.command.CancelOrderCommand;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

public class CancelOrderHandler  extends BasicCommandHandler<StockResultVO,CancelOrderCommand> {
	private static final Trace log = Trace.getLogger(CancelOrderHandler.class);

	@Override
    public void execute(final CancelOrderCommand command, final IAsyncCallback<StockResultVO> callback) {
    	getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).cancelOrder(command.getOrderID()).
    	onResult(new IAsyncCallback<StockResultVO>() {

			/* (non-Javadoc)
			 * @see com.wxxr.mobile.stock.trade.command.BasicCommandHandler.DelegateCallback#success(java.lang.Object)
			 */
			@Override
			public void success(StockResultVO result) {
                if (result.getSuccOrNot() == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("Failed cancel order, caused by "
                                + result.getCause());
                    }
                    callback.failed(new StockAppBizException(result.getCause()));
                }else{
                	callback.success(result);
                }
			}

			@Override
			public void failed(Throwable cause) {
				callback.failed(cause);
			}

			@Override
			public void cancelled() {
				callback.cancelled();
			}

			@Override
			public void setCancellable(ICancellable cancellable) {
				callback.setCancellable(cancellable);
			}
    	});
    }

}
