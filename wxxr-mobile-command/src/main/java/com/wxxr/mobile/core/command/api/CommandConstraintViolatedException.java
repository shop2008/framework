/**
 * 
 */
package com.wxxr.mobile.core.command.api;

import java.lang.annotation.Annotation;

/**
 * @author neillin
 *
 */
public class CommandConstraintViolatedException extends CommandException {

	private static final long serialVersionUID = 2700028276883254084L;
	
	private final Annotation constraint;

	public CommandConstraintViolatedException( Annotation ann) {
		super();
		this.constraint = ann;
	}

	public CommandConstraintViolatedException(String message,  Annotation ann) {
		super(message);
		this.constraint = ann;
	}

	/**
	 * @return the constraint
	 */
	public Annotation getConstraint() {
		return constraint;
	}

}
