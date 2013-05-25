package com.wxxr.mobile.core.rpc.impl;

import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "userArticleQueryBO")
public class UserArticleQueryBO  {
	
	private long userid ;
	private String queryDate;  //yyyy-MM-dd
	private String type;
	
	
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	
	
	public String getQueryDate() {
		return queryDate;
	}
	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	
}
