/**
 * 
 */
package com.wxxr.mobile.core.command.api;

import java.lang.annotation.Annotation;

/**
 * @author neillin
 *
 */
public class UserNotAuthorizedException extends
		CommandConstraintViolatedException {

	private static final long serialVersionUID = -8042836859630341295L;

	public UserNotAuthorizedException(Annotation ann) {
		super(ann);
	}

}
