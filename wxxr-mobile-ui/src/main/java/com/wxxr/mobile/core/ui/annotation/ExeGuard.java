/**
 * 
 */
package com.wxxr.mobile.core.ui.annotation;

/**
 * @author neillin
 *
 */
public @interface ExeGuard {
	int silentPeriod() default 500;
	String message() default "";
	String title() default "";
	boolean cancellable() default false;
	String sign() default "";
}
