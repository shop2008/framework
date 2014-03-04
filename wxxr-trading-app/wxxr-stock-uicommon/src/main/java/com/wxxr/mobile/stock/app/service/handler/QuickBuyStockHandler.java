package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.command.QuickBuyStockCommand;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

public class QuickBuyStockHandler extends BasicCommandHandler<StockResultVO,QuickBuyStockCommand>{
    private static final Trace log = Trace.register(QuickBuyStockHandler.class);

    @Override
    public void execute(final QuickBuyStockCommand command, final IAsyncCallback<StockResultVO> callback) {
    	QuickBuyStockCommand g = (QuickBuyStockCommand) command;
    	final float _capitalRate = Float.valueOf(g.getCapitalRate());
    	final long _stockBuyAmount = Long.valueOf(g.getStockBuyAmount());
    	final float _depositRate = Float.valueOf(g.getDepositRate());

    	getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).mulQuickBuy(
    			g.getCaptitalAmount(), _capitalRate, g.isVirtual(), g.getStockMarket(),
    			g.getStockCode(), _stockBuyAmount, _depositRate, g.getAssetType()).onResult(
    					new IAsyncCallback<StockResultVO>() {

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
    	     	                        log.debug("Failed quick buy stock, caused by "
    	    	                                + result.getCause());
    	     	                    }
    	    	                    callback.failed(new StockAppBizException(result.getCause()));
    	    	                }else{
    	    	                	callback.success(result);
    	    	                }
    	    				}
    	    	    	}
    					);

    }

}
