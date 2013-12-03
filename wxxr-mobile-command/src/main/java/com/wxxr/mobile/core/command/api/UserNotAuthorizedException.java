/**
 * 
 */
package com.wxxr.mobile.core.command.api;

import com.wxxr.mobile.core.command.annotation.ConstraintLiteral;

/**
 * @author neillin
 *
 */
public class UserNotAuthorizedException extends
		CommandConstraintViolatedException {

	private static final long serialVersionUID = -8042836859630341295L;

	public UserNotAuthorizedException(ConstraintLiteral ann) {
		super(ann);
	}

}
