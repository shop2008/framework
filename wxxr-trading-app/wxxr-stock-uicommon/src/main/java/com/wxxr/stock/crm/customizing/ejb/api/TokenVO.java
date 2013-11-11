package com.wxxr.stock.crm.customizing.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "TokenVO")
public class TokenVO implements Serializable{
	
	private static final long serialVersionUID = 5356517245748947098L;
	@XmlElement(name = "pollToken")
	private String pollToken;
	@XmlElement(name = "pushToken")
	private String pushToken;
	
	
	public String getPollToken() {
		return pollToken;
	}
	public void setPollToken(String pollToken) {
		this.pollToken = pollToken;
	}
	
	
	public String getPushToken() {
		return pushToken;
	}
	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}
}
