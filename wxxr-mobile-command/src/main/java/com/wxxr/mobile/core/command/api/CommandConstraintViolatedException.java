/**
 * 
 */
package com.wxxr.mobile.core.command.api;

import com.wxxr.mobile.core.command.annotation.ConstraintLiteral;

/**
 * @author neillin
 *
 */
public class CommandConstraintViolatedException extends CommandException {

	private static final long serialVersionUID = 2700028276883254084L;
	
	private final ConstraintLiteral constraint;

	public CommandConstraintViolatedException( ConstraintLiteral ann) {
		super();
		this.constraint = ann;
	}

	public CommandConstraintViolatedException(String message,  ConstraintLiteral ann) {
		super(message);
		this.constraint = ann;
	}

	/**
	 * @return the constraint
	 */
	public ConstraintLiteral getConstraint() {
		return constraint;
	}

}
