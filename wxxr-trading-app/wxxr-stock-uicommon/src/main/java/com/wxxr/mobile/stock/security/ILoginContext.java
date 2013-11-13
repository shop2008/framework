package com.wxxr.mobile.stock.security;

import javax.security.auth.login.LoginException;

public interface ILoginContext {
	public boolean login(String userName, String passwd) throws LoginException;
}
