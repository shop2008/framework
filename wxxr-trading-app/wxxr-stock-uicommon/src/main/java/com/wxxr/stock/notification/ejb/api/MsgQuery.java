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

@XmlRootElement(name="MsgQuery")
public class MsgQuery implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "id")
	String id;// 提醒ID
	
	public MsgQuery() {
		super();
	}

	public MsgQuery(String id) {
		super();
		this.id = id;
	}
		
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MsgQuery [id=").append(id).append("]");
		return builder.toString();
	}

}
