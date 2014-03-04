package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;

public class RefreshUserInfoCommand implements ICommand<UserVO>{

	@Override
	public String getCommandName() {
		return RefresUserInfoHandler.COMMAND_NAME;
	}

	@Override
	public Class<UserVO> getResultType() {
		
		return UserVO.class;
	}

	@Override
	public void validate() {
	}
	
}