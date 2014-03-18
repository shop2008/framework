/*
 * @(#)SearchUserCommandHandler.java 2014-3-17
 *
 * Copyright 2013-2014 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.mobile.stock.app.service.handler;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.stock.app.command.SearchUserCommand;
import com.wxxr.mobile.stock.app.service.handler.BasicCommandHandler.DelegateCallback;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.restful.resource.StockUserResourceAsync;
import com.wxxr.stock.restful.resource.UserResource;
import com.wxxr.stock.restful.resource.UserResourceAsync;

/**
 * 功能描述：
 * @author maruili
 * @createtime 2014-3-17 下午6:41:34
 */
public class SearchUserCommandHandler extends BasicCommandHandler<List<UserVO>,SearchUserCommand>{
	private static final Trace log = Trace
			.register("com.wxxr.mobile.stock.app.service.handler.SearchUserCommandHandler");
	public static final String COMMAND_NAME = "SearchUserInfoCommand";
	@Override
	public void execute(SearchUserCommand cmd, IAsyncCallback<List<UserVO>> callback) {
	
		getRestService(UserResourceAsync.class,UserResource.class).getUserByNickName(cmd.getUserParamVo()).onResult(callback);
		
	
//		onResult(new DelegateCallback<UserVO, List<UserVO>>(callback) {
//
//			@Override
//			protected List<UserVO> getTargetValue(UserVO vo) {
//				List<UserVO> list= null;
//				if(vo != null) {
//					list = new ArrayList<UserVO>();
//					list.add(vo);
//				}
//				return list;
//			}
//		});
		
	}

	



}
