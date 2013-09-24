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
@Target({ElementType.TYPE})
@Documented
@ModelAnnotation
public @interface ViewUI {
	String layout();
	Class<?> superClass() default Void.class;
	String modelName() default "";
}
