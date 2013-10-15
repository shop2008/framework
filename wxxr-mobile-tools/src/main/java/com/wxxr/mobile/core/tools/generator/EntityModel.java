/*
 * Copyright 2010-2013 WXXR Network Technology
 * Co. Ltd. All rights reserved. WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.mobile.core.tools.generator;

import com.wxxr.mobile.core.util.StringUtils;

/**
 * @class desc EntityModel.
 * @author wangxuyang
 * @version $Revision$
 * @created time 2013-9-26 下午6:26:29
 */
public class EntityModel extends BindableBeanModel{
	private String tblName;
	
	private String pkType;
	public String getTblName() {
		return tblName;
	}
	public void setTblName(String tblName) {
		this.tblName = tblName;
	}
	public String getPkType() {
		return pkType;
	}
	public void setPkType(String pkType) {
		this.pkType = pkType;
	}
}
