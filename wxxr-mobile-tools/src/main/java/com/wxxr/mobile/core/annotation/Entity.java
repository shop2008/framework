package com.wxxr.mobile.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
@Documented
@ModelAnnotation
public @interface Entity {
	String tblName();
	String daoIfClass();	
	Class<?> clazz() default Void.class;
}