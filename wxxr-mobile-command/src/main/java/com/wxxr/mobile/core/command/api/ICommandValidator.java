/**
 * 
 */
package com.wxxr.mobile.core.command.api;

/**
 * @author neillin
 *
 */
public interface ICommandValidator {
	void init(ICommandExecutionContext ctx);
	void checkCommandConstraints(ICommand<?> command) throws CommandConstraintViolatedException;
	void destroy();
}
