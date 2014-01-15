package com.wxxr.mobile.stock.app.model;

import com.wxxr.javax.validation.constraints.NotNull;
import com.wxxr.javax.validation.constraints.Size;
import com.wxxr.mobile.stock.app.validate.ChineseChars;

public class UserNickSetCallBack {
	
	@NotNull
	@ChineseChars
	@Size(min=2,max=6)
	private String nickName;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
