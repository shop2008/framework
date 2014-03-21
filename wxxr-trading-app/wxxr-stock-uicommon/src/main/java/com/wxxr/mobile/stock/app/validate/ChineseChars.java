package com.wxxr.mobile.stock.app.validate;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.wxxr.javax.validation.Constraint;
import com.wxxr.javax.validation.Payload;


@Constraint(validatedBy = { ChineseCharsValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented
public @interface ChineseChars {
	public abstract String message() default "{com.wxxr.mobile.stock.app.ChineseChars.message}";

	public abstract Class<?>[] groups() default { };

	public abstract Class<? extends Payload>[] payload() default { };
}
