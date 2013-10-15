/*
 * Copyright 2010-2013 WXXR Network Technology
 * Co. Ltd. All rights reserved. WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.mobile.core.annotation;

/**
 * @class desc DBCommand.
 * @author wangxuyang
 * @version $Revision$
 * @created time 2013-10-10 下午2:32:48
 */
public @interface DBCommand {
	Class<?> clazz();
	String description();
	String name();
}
