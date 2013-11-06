/*
 * Copyright 2010-2013 WXXR Network Technology
 * Co. Ltd. All rights reserved. WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.mobile.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @class desc Table.
 * @author wangxuyang
 * @version $Revision$
 * @created time 2013-10-15 下午4:10:52
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
@Documented
public @interface Table {
	String name()  default "";
	String catalog() default "";
	String schema() default "";
}
