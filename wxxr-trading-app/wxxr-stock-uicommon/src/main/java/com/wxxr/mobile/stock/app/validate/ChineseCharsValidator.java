package com.wxxr.mobile.stock.app.validate;

import java.util.regex.Pattern;

import com.wxxr.javax.validation.ConstraintValidator;
import com.wxxr.javax.validation.ConstraintValidatorContext;

public class ChineseCharsValidator implements ConstraintValidator<ChineseChars,String> {

	public void initialize(ChineseChars annotation) {
	}

	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if(value == null)
			return true;
		else {
			return	Pattern.matches("[\u4E00-\u9FA5]+", value);
		}
	}
}
