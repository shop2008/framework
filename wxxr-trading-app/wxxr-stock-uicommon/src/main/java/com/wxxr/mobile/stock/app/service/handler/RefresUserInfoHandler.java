/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.restful.resource.StockUserResourceAsync;

/**
 * @author wangyan
 *
 */
public class RefresUserInfoHandler extends BasicCommandHandler<UserVO,RefreshUserInfoCommand> {

	
	public static final String COMMAND_NAME = "RefreshUserInfoCommand";

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public void execute(final RefreshUserInfoCommand command, IAsyncCallback<UserVO> callback) {
		getRestService(StockUserResourceAsync.class,StockUserResource.class).getUser().onResult(callback);
	}

}
