package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.stock.common.valobject.ResultBaseVO;

public class UpPwdCommand implements ICommand<ResultBaseVO>{

	private String newPwd;
	private	String newPwd2;
	private String oldPwd;
	
	
	/**
	 * @return the newPwd
	 */
	public String getNewPwd() {
		return newPwd;
	}

	/**
	 * @param newPwd the newPwd to set
	 */
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	/**
	 * @return the newPwd2
	 */
	public String getNewPwd2() {
		return newPwd2;
	}

	/**
	 * @param newPwd2 the newPwd2 to set
	 */
	public void setNewPwd2(String newPwd2) {
		this.newPwd2 = newPwd2;
	}

	/**
	 * @return the oldPwd
	 */
	public String getOldPwd() {
		return oldPwd;
	}

	/**
	 * @param oldPwd the oldPwd to set
	 */
	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#getCommandName()
	 */
	@Override
	public String getCommandName() {
		return UpPwdHandler.COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#getResultType()
	 */
	@Override
	public Class<ResultBaseVO> getResultType() {
		return ResultBaseVO.class;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#validate()
	 */
	@Override
	public void validate() {
		if(StringUtils.isBlank(oldPwd)){
			throw new CommandException("旧密码不能为空");
		}
		if(StringUtils.isBlank(newPwd)){
			throw new CommandException("新密码不能为空");
		}
		if(!newPwd.equals(newPwd2)){
			throw new CommandException("两次密码不一致");
		}
	}
	
}