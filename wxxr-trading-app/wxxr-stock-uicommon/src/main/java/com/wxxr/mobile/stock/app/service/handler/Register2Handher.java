package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.command.UserRegister2Command;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.stock.restful.json.RegQueryVO;
import com.wxxr.stock.restful.resource.UserResource;
import com.wxxr.stock.restful.resource.UserResourceAsync;

public class Register2Handher extends BasicCommandHandler<SimpleResultVo, UserRegister2Command> {

	@Override
	public void execute(UserRegister2Command cmd,
			IAsyncCallback<SimpleResultVo> callabck) {
		RegQueryVO vo = new RegQueryVO();
		vo.setUsername(cmd.getUserName());
		vo.setPassword(cmd.getPassword());
		getRestService(UserResourceAsync.class,UserResource.class).registerWithPassword(vo).onResult(callabck);
	}

}
