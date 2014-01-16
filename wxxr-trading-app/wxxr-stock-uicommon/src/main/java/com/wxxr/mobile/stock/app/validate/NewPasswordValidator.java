package com.wxxr.mobile.stock.app.validate;

import com.wxxr.javax.validation.ConstraintValidator;
import com.wxxr.javax.validation.ConstraintValidatorContext;
import com.wxxr.mobile.core.util.ObjectUtils;
import com.wxxr.mobile.stock.app.model.UserAlterPswCallback;

public class NewPasswordValidator implements ConstraintValidator<NewPassword,UserAlterPswCallback> {

	public void initialize(NewPassword annotation) {
	}

	public boolean isValid(UserAlterPswCallback value, ConstraintValidatorContext constraintValidatorContext) {
		if(value == null)
			return true;
		else {
			return	ObjectUtils.isEquals(value.getNewPassword(), value.getNewPasswordAgain());
		}
	}
}
