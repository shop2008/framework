/**
 * 
 */
package com.wxxr.mobile.core.ui.annotation;

import com.wxxr.javax.validation.groups.Default;

/**
 * @author neillin
 *
 */
public @interface BeanValidation {
	String bean();
	Class<?> group() default Default.class;
	String message();
}
