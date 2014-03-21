package com.wxxr.security.vo;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UserPermisVO")
public class UserPermisVO {
	@XmlElement(name = "userid")
	private String userid;
	@XmlElement(name = "permis")
	private String permis;
	
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPermis() {
		return permis;
	}
	public void setPermis(String permis) {
		this.permis = permis;
	}
}
