package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.security.vo.SimpleResultVo;

public class UserRegisterCommand implements ICommand<SimpleResultVo>{
	private String userName;
	@Override
	public String getCommandName() {
		return RegisterHandher.COMMAND_NAME;
	}

	@Override
	public Class<SimpleResultVo> getResultType() {
		return SimpleResultVo.class;
	}

	@Override
	public void validate() {
		if(!StringUtils.isNumeric(userName)||userName.length()!=11){
			throw new CommandException("请输入正确的手机号");
		}
		
	}

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
	
	
}