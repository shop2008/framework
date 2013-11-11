package com.wxxr.security.vo;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RegistResultVO")
public class RegistResultVO implements Serializable {
	@XmlElement(name = "message")
	private String message;
	@XmlElement(name = "success")
	private boolean success;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
