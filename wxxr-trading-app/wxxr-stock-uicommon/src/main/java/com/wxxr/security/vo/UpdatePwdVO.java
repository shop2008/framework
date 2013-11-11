package com.wxxr.security.vo;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UpdatePwdVO")
public class UpdatePwdVO implements Serializable{

	private static final long serialVersionUID = 1L;
	@XmlElement(name = "password")
	private String password;
	@XmlElement(name = "oldPwd")
	private String oldPwd;

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
	
	
}
