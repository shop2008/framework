package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.command.SignUpCommand;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.UserSignVO;

public class SignUpHandler extends BasicCommandHandler<UserSignVO, SignUpCommand> {

	
	@Override
	public void execute(SignUpCommand cmd, IAsyncCallback<UserSignVO> callback) {
		getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).userSign().onResult(callback);
	}


}
