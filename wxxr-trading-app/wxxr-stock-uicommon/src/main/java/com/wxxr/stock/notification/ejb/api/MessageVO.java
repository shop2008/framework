/*
 * @(#)MessageVO.java	 2012-3-23
 *
 * Copyright 2004-2012 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.stock.notification.ejb.api;

import java.io.Serializable;
import java.util.Map;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="message")
public class MessageVO implements Serializable {

	private static final long serialVersionUID = 1L;
	@XmlElement(name = "id")
	String id;// 提醒ID
	@XmlElement(name = "type")
	String type;// 提醒类型
	@XmlElement(name = "mcode")
	String mcode;// 市场代码
	@XmlElement(name = "qcode")
	String qcode;// 股票代码
	@XmlElement(name = "content")
	String content;// 提醒内容
	@XmlElement(name = "createdDate")
	String createdDate;
	@XmlElement(name = "attributes")
	protected Map<String, String> attributes ;
	
	public MessageVO() {
		// FIXME MessageVO constructor
		super();
	}

	public MessageVO(String id, String type, String mcode, String qcode,
			String content,String createdDate) {
		super();
		this.id = id;
		this.type = type;
		this.mcode = mcode;
		this.qcode = qcode;
		this.content = content;
		this.createdDate=createdDate;
	}
	
	
	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMcode() {
		return mcode;
	}

	public void setMcode(String mcode) {
		this.mcode = mcode;
	}

	public String getQcode() {
		return qcode;
	}

	public void setQcode(String qcode) {
		this.qcode = qcode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * @return the attributes
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return new StringBuffer("Id=")
		.append(this.getId())
		.append("; Mcode=")
		.append(this.getMcode())
		.append("; Qcode=")
		.append(this.getQcode())
		.append("; Type=")
		.append(this.getType())
		.append("; Content=")
		.append(this.getContent()).toString();
	}
}
