/**
 * 
 */
package com.wxxr.mobile.core.command.api;

import com.wxxr.mobile.core.command.annotation.ConstraintLiteral;

/**
 * @author neillin
 *
 */
public class RequiredNetNotAvailablexception extends
		CommandConstraintViolatedException {

	private static final long serialVersionUID = -4803874472598447075L;

	public RequiredNetNotAvailablexception(ConstraintLiteral ann) {
		super(ann);
	}

}
