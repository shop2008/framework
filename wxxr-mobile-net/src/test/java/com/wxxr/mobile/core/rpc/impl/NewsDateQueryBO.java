package com.wxxr.mobile.core.rpc.impl;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "NewsDateQueryBO")
public class NewsDateQueryBO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String date ;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
