/**
 * 
 */
package com.wxxr.mobile.core.ui.annotation;

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
@PresentationModel
public @interface Bean {
	BindingType type() default BindingType.Pojo;
	String express() default "";
	boolean nullable() default false;
	
	public enum BindingType {
		Service,
		Pojo
	}
}
