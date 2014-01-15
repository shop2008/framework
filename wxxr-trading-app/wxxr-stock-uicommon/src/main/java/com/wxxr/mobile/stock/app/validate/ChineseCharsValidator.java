package com.wxxr.mobile.stock.app.validate;

import com.wxxr.javax.validation.ConstraintValidator;
import com.wxxr.javax.validation.ConstraintValidatorContext;

public class ChineseCharsValidator implements ConstraintValidator<ChineseChars,String> {

	public void initialize(ChineseChars annotation) {
	}

	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		return true;
	}
}
