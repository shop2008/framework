/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.restful.resource.StockUserResourceAsync;

/**
 * @author wangyan
 *
 */
public class GetPushMessageSettingHandler extends BasicCommandHandler<SimpleResultVo,GetPushMessageSettingCommand> {

	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public void execute(final GetPushMessageSettingCommand command,IAsyncCallback<SimpleResultVo> callback) {
		getRestService(StockUserResourceAsync.class,StockUserResource.class).isBindApp().onResult(callback);
	}
}

