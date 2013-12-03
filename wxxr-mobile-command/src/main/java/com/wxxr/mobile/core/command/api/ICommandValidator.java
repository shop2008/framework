/**
 * 
 */
package com.wxxr.mobile.core.command.api;

import com.wxxr.mobile.core.command.annotation.ConstraintLiteral;

/**
 * @author neillin
 *
 */
public interface ICommandValidator {
	void init(ICommandExecutionContext ctx);
	void checkCommandConstraints(ICommand<?> command) throws CommandConstraintViolatedException;
	void validationConstraints(ConstraintLiteral... constraints);
	void destroy();
}
