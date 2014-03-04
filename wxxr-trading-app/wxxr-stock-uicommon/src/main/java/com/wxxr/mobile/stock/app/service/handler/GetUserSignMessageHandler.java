package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.command.GetUserSignMessageCommand;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.UserSignVO;

public class GetUserSignMessageHandler extends BasicCommandHandler<UserSignVO, GetUserSignMessageCommand> {

	
	@Override
	public void execute(GetUserSignMessageCommand cmd, IAsyncCallback<UserSignVO> callback) {
		getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).getUserSignMessage().onResult(callback);
	}


}
