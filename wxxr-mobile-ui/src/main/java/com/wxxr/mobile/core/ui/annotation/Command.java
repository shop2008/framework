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
@Target({ElementType.METHOD})
@Documented
@PresentationModel
public @interface Command {
	String commandName() default "";
	String navigateMethod() default "";
	String description() default "";
	String enableWhen() default "";
	String visibleWhen() default "";
	Navigation[] navigations() default {};
	UIItem[] uiItems() default {};
	BeanValidation[] validations() default {};
	FieldUpdating[] updateFields() default {};
}
