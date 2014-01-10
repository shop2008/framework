package com.wxxr.mobile.stock.app.event;

import com.wxxr.mobile.core.event.api.GenericEventObject;
import com.wxxr.mobile.core.security.api.IUserLoginEvent;
import com.wxxr.mobile.core.security.api.LoginAction;

public class UserLoginEvent extends GenericEventObject implements IUserLoginEvent {
	private final LoginAction action;
	private final String userId;
	
	public UserLoginEvent(String userId, LoginAction act){
		this.userId = userId;
		this.action = act;
	}

	public LoginAction getAction() {
		return action;
	}

	public String getUserId() {
		return userId;
	}


}
