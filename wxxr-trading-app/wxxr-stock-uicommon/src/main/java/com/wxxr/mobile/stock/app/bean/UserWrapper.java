/*
 * @(#)UserWrapper.java 2014-3-18
 *
 * Copyright 2013-2014 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.mobile.stock.app.bean;

import com.wxxr.stock.crm.customizing.ejb.api.UserVO;

/**
 * 功能描述：
 * @author maruili
 * @createtime 2014-3-18 上午10:45:10
 */
public class UserWrapper {
	private UserVO user;

	public UserWrapper(UserVO user) {
		if (user==null) {
			throw new IllegalArgumentException();
		}
		this.user = user;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return user.getUserName();
	}
	
	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return user.getNickName();
	}
	
	/**
	 * @return the moblie
	 */
	public String getMoblie() {
		return user.getMoblie();
	}
}
