/**
 * 
 */
package com.wxxr.mobile.core.command.api;

import java.lang.annotation.Annotation;

/**
 * @author neillin
 *
 */
public class UserLoginRequiredException extends
		CommandConstraintViolatedException {

	private static final long serialVersionUID = -7748662050296439546L;

	public UserLoginRequiredException(Annotation ann) {
		super(ann);
	}

}
