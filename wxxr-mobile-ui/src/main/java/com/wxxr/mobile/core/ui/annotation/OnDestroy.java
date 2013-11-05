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
 * 
 * Annotate method to indicate the method must be invoked before this view is destroyed
 * 
 * @author neillin
 *
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
@Documented
@PresentationModel
public @interface OnDestroy {
}
