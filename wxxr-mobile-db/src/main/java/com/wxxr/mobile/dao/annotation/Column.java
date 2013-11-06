package com.wxxr.mobile.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(value={ElementType.METHOD,ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Column {
	  String name() default "";
	  
	  boolean unique() default false;
	  
	  boolean nullable() default true;
	  
	  boolean insertable() default true;
	  
	  boolean updatable() default true;
	  
	  String columnDefinition() default "";
	  
	  String table() default "";
	  
	  int length() default (int) 255;
	  
	  int precision() default (int) 0;
	  
	  int scale() default (int) 0;
}
