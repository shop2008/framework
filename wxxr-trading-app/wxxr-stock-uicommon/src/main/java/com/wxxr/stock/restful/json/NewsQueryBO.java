package com.wxxr.stock.restful.json;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "newsquery")
public class NewsQueryBO /*extends AuditableLKeyObject*/ {
	@XmlElement(name = "start")
	private int start;
	@XmlElement(name = "limit")
    private int limit;
	@XmlElement(name = "type")
    private String type;
	@XmlElement(name = "uid")
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
