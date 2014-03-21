package com.wxxr.mobile.stock.app.validate;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.wxxr.javax.validation.Constraint;
import com.wxxr.javax.validation.Payload;


@Constraint(validatedBy = { NewPasswordValidator.class })
@Target({ TYPE })
@Retention(RUNTIME)
@Documented
public @interface NewPassword {
	public abstract String message() default "{com.wxxr.mobile.stock.app.NewPassword.message}";

	public abstract Class<?>[] groups() default { };

	public abstract Class<? extends Payload>[] payload() default { };
}
