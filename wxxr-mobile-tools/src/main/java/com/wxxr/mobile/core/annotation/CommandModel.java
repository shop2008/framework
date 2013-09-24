/**
 * 
 */
package com.wxxr.mobile.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wxxr.mobile.core.ui.api.IUICommand;

/**
 * @author neillin
 *
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD})
@Documented
@ModelAnnotation
public @interface CommandModel {
	Class<? extends IUICommand> implClass() default DummyCommand.class;
	String actionExpr() default "";
	Class<?> validationGroup() default Void.class;
	String enableWhen() default "";
	String visibleWhen() default "";
	Navigation[] navigations() default {};
}
