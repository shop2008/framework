//$Id$
package org.hibernate.validator.constraints.impl;

import org.hibernate.validator.constraints.CreditCardNumber;

import com.wxxr.javax.validation.ConstraintValidator;
import com.wxxr.javax.validation.ConstraintValidatorContext;

/**
 * Check a credit card number through the Luhn algorithm.
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
public class CreditCardNumberValidator implements ConstraintValidator<CreditCardNumber, String> {
	private LuhnValidator luhnValidator;

	public CreditCardNumberValidator() {
		luhnValidator = new LuhnValidator( 2 );
	}

	public void initialize(CreditCardNumber annotation) {
	}

	public boolean isValid(String value, ConstraintValidatorContext context) {
		if ( value == null ) {
			return true;
		}
		return luhnValidator.passesLuhnTest( value );
	}
}