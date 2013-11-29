/*
 * @(#)NodeDigestVO.java	 Mar 23, 2012
 *
 * Copyright 2004-2012 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.sync.client.dto;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ResultBaseVO")
public class NodeDigestVO implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "resultInfo")
	private String resultInfo;
	@XmlElement(name = "resulttype")
	private int resulttype;
	
	public String getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
	}
	
	public int getResulttype() {
		return resulttype;
	}

	public void setResulttype(int resulttype) {
		this.resulttype = resulttype;
	}

	@Override
	public String toString() {
		return "ResultBaseVO [resultInfo=" + resultInfo + ", resulttype=" + resulttype + "]";
	}
	
}
