/**
 * 
 */
package com.wxxr.mobile.core.ui.annotation;

/**
 * @author neillin
 *
 */
public @interface Async {
	int silentPeriod() default 500;
	int timeout() default -1;
	String message() default "";
	boolean cancellable() default false;
	String sign() default "";
}
