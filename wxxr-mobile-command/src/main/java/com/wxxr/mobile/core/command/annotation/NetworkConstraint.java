/**
 * 
 */
package com.wxxr.mobile.core.command.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author neillin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@CommandConstraint(validatedBy={})
public @interface NetworkConstraint {
	NetworkConnectionType[] allowConnectionTypes() default {};	// default means any connection is allowed
}
