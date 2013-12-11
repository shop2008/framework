package com.wxxr.mobile.stock.app.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wxxr.mobile.core.command.annotation.CommandConstraint;
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
@Documented
@CommandConstraint(validatedBy={})
public @interface TadingTimeConstraint {
    String market() default "";
}
 