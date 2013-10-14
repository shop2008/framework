/**
 * 
 */
package com.wxxr.mobile.core.ui.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wxxr.mobile.core.ui.common.PageBase;

/**
 * @author neillin
 *
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
@Documented
@PresentationModel
public @interface Page {
	Class<? extends PageBase> superClass() default PageBase.class;
	String name();
	String description() default "";
	AttributeKey[] attrTypes() default {};
	Attribute[] attributes() default {};
}
