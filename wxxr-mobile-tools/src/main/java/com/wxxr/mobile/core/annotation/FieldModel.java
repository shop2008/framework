/**
 * 
 */
package com.wxxr.mobile.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author neillin
 *
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD})
@Documented
@ModelAnnotation
public @interface FieldModel {
	String propertyExpr() default "";
	String getExpr() default "";
	String setExpr() default "";
	boolean lazyValidation() default true;
}
