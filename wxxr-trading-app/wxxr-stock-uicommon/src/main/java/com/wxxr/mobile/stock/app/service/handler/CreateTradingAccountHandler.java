package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.Async;
import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.command.CreateTradingAccountCommand;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

public class CreateTradingAccountHandler extends BasicCommandHandler<StockResultVO,CreateTradingAccountCommand>{
    private static final Trace log = Trace.register(CreateTradingAccountHandler.class);
    @Override
    public void execute(final CreateTradingAccountCommand g, final IAsyncCallback<StockResultVO> callback) {
        Async<StockResultVO> vo = null;
        if (StringUtils.isBlank(g.getTrdingType())) {
        	vo = getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class)
                    .mulCreateTradingAccount(g.getCaptitalAmount(), g.getCapitalRate(),
                            g.isVirtual(), g.getDepositRate(),g.getAssetType());
		}else{
			vo = getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).newCreateTradingAccount(g.getCaptitalAmount(), g.getCapitalRate(),  g.isVirtual(),g.getDepositRate(),g.getAssetType(), g.getTrdingType());
		}
		vo.onResult(new IAsyncCallback<StockResultVO>() {

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
     	                        log.debug("Failed create trading account, caused by "
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

