/*
 * @(#)SearchUserCommand.java 2014-3-17
 *
 * Copyright 2013-2014 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.service.handler.SearchUserCommandHandler;
import com.wxxr.security.vo.UserParamVO;
import com.wxxr.stock.crm.customizing.ejb.api.SearchNickNameVO;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;

/**
 * 功能描述：
 * @author maruili
 * @createtime 2014-3-17 下午4:44:28
 */
public class SearchUserCommand implements ICommand<SearchNickNameVO>{
	
	private UserParamVO userParamVo;
	
	

	public UserParamVO getUserParamVo() {
		return userParamVo;
	}

	public void setUserParamVo(UserParamVO userParamVo) {
		this.userParamVo = userParamVo;
	}

	@Override
	public String getCommandName() {
		return SearchUserCommandHandler.COMMAND_NAME;
	}

	

	@Override
	public void validate() {
		
		
	}

	@Override
	public Class<SearchNickNameVO> getResultType() {
		return SearchNickNameVO.class;
	}

}
