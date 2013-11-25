/**
 * 
 */
package com.wxxr.mobile.core.command.api;

import java.util.concurrent.Future;

/**
 * @author neillin
 *
 */
public interface ICommandExecutor {
	<T> Future<T> submitCommand(ICommand<T> command);
	ICommandExecutor registerCommandHandler(String cmdName,ICommandHandler handler);
	ICommandExecutor unregisterCommandHandler(String cmdName,ICommandHandler handler);
	ICommandExecutor registerCommandValidator(ICommandValidator validator);
	ICommandExecutor unregisterCommandValidator(ICommandValidator validator);
}
