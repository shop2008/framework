package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.command.ClearTradingAccountCommand;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

public class ClearTradingAccountHandler extends BasicCommandHandler<StockResultVO,ClearTradingAccountCommand> {
	private static final Trace log = Trace.getLogger(ClearTradingAccountHandler.class);

    @Override
    public void execute(final ClearTradingAccountCommand command, final IAsyncCallback<StockResultVO> callback) {
    	ClearTradingAccountCommand g = (ClearTradingAccountCommand) command;
    	getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).clearTradingAccount(g.getAcctID()).
    	onResult(new IAsyncCallback<StockResultVO>() {

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

			/* (non-Javadoc)
			 * @see com.wxxr.mobile.stock.trade.command.BasicCommandHandler.DelegateCallback#success(java.lang.Object)
			 */
			@Override
			public void success(StockResultVO result) {
                if (result.getSuccOrNot() == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("Failed clear account, caused by "
                                + result.getCause());
                    }
                    callback.failed(new StockAppBizException(result.getCause()));
                }else{
                	callback.success(result);
                }
			}
    	});
    }

}
