package com.wxxr.security.vo;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "VerifyQuery")
public class VerifyVO {

	private String passwd ;

	
	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
}
