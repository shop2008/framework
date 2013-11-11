/*
 * @(#)FeedBackMessageVO.java	 Apr 1, 2012
 *
 * Copyright 2004-2012 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.stock.restful.json;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PushToken")
public class PushTokenVO implements Serializable{

	private String token;
	private Long userId;

	public PushTokenVO() {
		super();
	}

	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
