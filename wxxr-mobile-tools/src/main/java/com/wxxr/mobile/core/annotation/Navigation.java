/**
 * 
 */
package com.wxxr.mobile.core.annotation;

/**
 * @author neillin
 *
 */
public @interface Navigation {
	String on();
	String message() default "";
	String showView() default "";
	String showPage() default "";
}
