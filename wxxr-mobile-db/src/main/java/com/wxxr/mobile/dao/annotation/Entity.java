package com.wxxr.mobile.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wxxr.mobile.core.annotation.ModelAnnotation;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
@Documented
@ModelAnnotation
public @interface Entity {
}