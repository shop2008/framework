package com.wxxr.mobile.core.common;

import com.wxxr.mobile.core.event.api.GenericEventObject;
import com.wxxr.mobile.core.security.api.IUserLoginEvent;
import com.wxxr.mobile.core.security.api.LoginAction;

class TestLoginEvent extends GenericEventObject implements IUserLoginEvent {

	private final LoginAction action;
	private final String userId;
	
	public TestLoginEvent(String userId, LoginAction act){
		this.userId = userId;
		this.action = act;
	}
	
	@Override
	public LoginAction getAction() {
		return this.action;
	}

	@Override
	public String getUserId() {
		return this.userId;
	}
	
}
