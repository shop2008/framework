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
 * Annotate method to indicate the method must be invoked when this view receives ValueChangedEvent(s),
 * the annotated method must take a parameter with type of ValueChangedEvent
 * 
 * @author neillin
 *
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
@Documented
@PresentationModel
public @interface OnDataChanged {
}
