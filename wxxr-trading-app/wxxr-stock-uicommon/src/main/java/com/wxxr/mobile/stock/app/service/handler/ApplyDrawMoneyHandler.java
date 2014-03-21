/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

/**
 * @author wangyan
 *
 */
public class ApplyDrawMoneyHandler extends BasicCommandHandler<StockResultVO,ApplyDrawMoneyCommand> {

	public static final String COMMAND_NAME="ApplyDrawMoneyCommand";
	
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public void execute(final ApplyDrawMoneyCommand cmd, IAsyncCallback<StockResultVO> callback) {
		ApplyDrawMoneyCommand command=(ApplyDrawMoneyCommand)cmd;
		getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).drawMoney(command.getAmount()).onResult(callback);
	}


}
