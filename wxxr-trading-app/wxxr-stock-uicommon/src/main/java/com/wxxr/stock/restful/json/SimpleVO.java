/*
 * @(#)SimpleData.java	 2012-4-27
 *
 * Copyright 2004-2012 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.stock.restful.json;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "simple")
public class SimpleVO implements Serializable {
    /** The serialVersionUID */
	private static final long serialVersionUID = 1L;
	private String data;

	public SimpleVO() {
		super();
	}
	public SimpleVO(String data) {
		super();
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
    
}
