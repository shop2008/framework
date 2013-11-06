/*
 * Copyright 2010-2013 WXXR Network Technology
 * Co. Ltd. All rights reserved. WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.mobile.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @class desc OneToOne.
 * @author wangxuyang
 * @version $Revision$
 * @created time 2013-10-16 上午9:48:32
 */
@Target(value = { ElementType.METHOD, ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface OneToOne {

	public Class targetEntity() default void.class;

	public boolean optional() default true;

	public String mappedBy() default "";

}