package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.stock.restful.resource.UserResource;
import com.wxxr.stock.restful.resource.UserResourceAsync;

public class RegisterHandher extends BasicCommandHandler<SimpleResultVo,UserRegisterCommand> {

	public static final String COMMAND_NAME = "UserRegisterCommand";

	
	@Override
	public void execute(final UserRegisterCommand command, IAsyncCallback<SimpleResultVo> callback) {
		getRestService(UserResourceAsync.class,UserResource.class).register(command.getUserName()).
		onResult(callback);
	}

}
