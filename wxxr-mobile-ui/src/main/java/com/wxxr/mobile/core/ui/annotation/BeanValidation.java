/**
 * 
 */
package com.wxxr.mobile.core.ui.annotation;

/**
 * @author neillin
 *
 */
public @interface BeanValidation {
	String bean();
	Class<?>[] groups() default {};
}
