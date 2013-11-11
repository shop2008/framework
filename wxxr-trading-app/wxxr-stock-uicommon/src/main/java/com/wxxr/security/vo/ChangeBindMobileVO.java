package com.wxxr.security.vo;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ChangeBindMobileVO")
public class ChangeBindMobileVO {
	@XmlElement(name = "newBindMobile")
	private String newBindMobile ;
	@XmlElement(name = "verifCode")
	private String verifCode ;     	
	
	
	public String getNewBindMobile() {
		return newBindMobile;
	}
	public void setNewBindMobile(String newBindMobile) {
		this.newBindMobile = newBindMobile;
	}
	
	public String getVerifCode() {
		return verifCode;
	}
	public void setVerifCode(String verifCode) {
		this.verifCode = verifCode;
	}
	
	
}
