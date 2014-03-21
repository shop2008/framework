package com.wxxr.mobile.stock.app.model;

import com.wxxr.javax.validation.constraints.NotNull;
import com.wxxr.javax.validation.constraints.Size;

public class UserRegCallback {
	
	@NotNull
	private String userName;

	@NotNull
	@Size(min=6,max=12)
	private String password;
	
	@NotNull
	@Size(min=6,max=12)
	private String retypePassword;
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRetypePassword() {
		return retypePassword;
	}

	public void setRetypePassword(String retypePassword) {
		this.retypePassword = retypePassword;
	}
}
