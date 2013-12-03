/**
 * 
 */
package com.wxxr.mobile.core.command.api;

import com.wxxr.mobile.core.command.annotation.ConstraintLiteral;

/**
 * @author neillin
 *
 */
public class UserLoginRequiredException extends
		CommandConstraintViolatedException {

	private static final long serialVersionUID = -7748662050296439546L;

	public UserLoginRequiredException(ConstraintLiteral ann) {
		super(ann);
	}

}
