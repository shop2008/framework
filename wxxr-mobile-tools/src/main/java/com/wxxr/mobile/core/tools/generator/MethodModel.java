/*
 * Copyright 2010-2013 WXXR Network Technology
 * Co. Ltd. All rights reserved. WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.mobile.core.tools.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @class desc MethodModel.
 * @author wangxuyang
 * @version $Revision$
 * @created time 2013-10-10 上午11:33:39
 */
public class MethodModel {
	private List<String> methods = new ArrayList<String>();
	private List<String> imports;
	public List<String> getImports() {
		return imports == null ? Collections.EMPTY_LIST : imports;
	}

}
