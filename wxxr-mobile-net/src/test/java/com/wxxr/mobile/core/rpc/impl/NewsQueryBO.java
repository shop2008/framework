package com.wxxr.mobile.core.rpc.impl;

import com.wxxr.javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "newsquery")
public class NewsQueryBO {
    private int start;
    private int limit;
    private String type;
    
    private int uid;
    
	
    public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	

    
}
