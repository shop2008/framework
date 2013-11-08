package com.wxxr.security.vo;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UserBaseInfoVO")
public class UserBaseInfoVO implements Serializable {
	@XmlElement(name = "userName")
	private String userName;
	@XmlElement(name = "bindPhoneNum")
	private String bindPhoneNum;
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBindPhoneNum() {
		return bindPhoneNum;
	}

	public void setBindPhoneNum(String bindPhoneNum) {
		this.bindPhoneNum = bindPhoneNum;
	}

	
}
