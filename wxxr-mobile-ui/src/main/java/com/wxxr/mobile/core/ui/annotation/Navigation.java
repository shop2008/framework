/**
 * 
 */
package com.wxxr.mobile.core.ui.annotation;

/**
 * @author neillin
 *
 */
@PresentationModel
public @interface Navigation {
	String on();
	String message() default "";
	String showView() default "";
	String showPage() default "";
	Parameter[] params() default {};
}
