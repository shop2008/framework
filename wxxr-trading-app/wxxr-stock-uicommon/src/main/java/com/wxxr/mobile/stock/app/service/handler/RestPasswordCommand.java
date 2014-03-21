package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.util.StringUtils;

@NetworkConstraint
public class RestPasswordCommand implements ICommand<Object>{

	private String userName;
	
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String getCommandName() {
		return RestPasswordHandler.COMMAND_NAME;
	}

	@Override
	public Class<Object> getResultType() {
		return Object.class;
	}

	@Override
	public void validate() {
		if(StringUtils.isBlank(userName)){
			throw new CommandException("用户名不能为空");
		}
	}
	
}