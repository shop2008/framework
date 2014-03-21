package com.wxxr.mobile.stock.app.service.handler;

import java.util.regex.Pattern;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.stock.common.valobject.ResultBaseVO;
@NetworkConstraint(allowConnectionTypes={})
@SecurityConstraint(allowRoles = {})
public class UpdateNickNameCommand implements ICommand<ResultBaseVO>{

	private String nickName;
	
	
	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Override
	public String getCommandName() {
		return UpdateNickNameHandler.COMMAND_NAME;
	}

	@Override
	public Class<ResultBaseVO> getResultType() {
		return ResultBaseVO.class;
	}

	@Override
	public void validate() {
		if(StringUtils.isBlank(nickName)){
			throw new CommandException("昵称不能为空");
		}
		
		
		if (!Pattern.matches("[\u4E00-\u9FA5]{2,6}", nickName)) {
			throw new CommandException("昵称必须为2到6个中文字符！");
		}
		
	}
	
}