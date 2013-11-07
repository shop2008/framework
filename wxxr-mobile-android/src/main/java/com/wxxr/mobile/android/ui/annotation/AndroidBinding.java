/**
 * 
 */
package com.wxxr.mobile.android.ui.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wxxr.mobile.android.ui.AndroidBindingType;

/**
 * @author neillin
 *
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
@Documented
public @interface AndroidBinding {
	AndroidBindingType type();
	String layoutId();
	String description() default "";
	String superClassName() default "";
	boolean withToolbar() default false;
}
