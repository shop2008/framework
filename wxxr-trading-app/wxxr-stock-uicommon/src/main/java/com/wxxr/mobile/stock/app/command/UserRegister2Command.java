package com.wxxr.mobile.stock.app.command;

import java.util.regex.Pattern;

import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.security.vo.SimpleResultVo;

public class UserRegister2Command implements ICommand<SimpleResultVo>{
	public static final String COMMAND_NAME = "UserRegister2Command";
	private static String passwordRegExp = "^[\\@A-Za-z0-9\\!\\#\\$\\%\\^\\&\\*\\.\\~]{6,12}$";

	private String userName;
	private String password;
	
	@Override
	public String getCommandName() {
		return COMMAND_NAME;
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
		if(StringUtils.isBlank(password)||!Pattern.matches(passwordRegExp, password)){
			throw new CommandException("无效的密码，请输入6-12位密码");
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
