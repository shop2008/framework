/**
 * 
 */
package com.wxxr.mobile.core.command.api;

import java.util.concurrent.Future;

import com.wxxr.mobile.core.util.IAsyncCallback;

/**
 * @author neillin
 *
 */
public interface ICommandExecutor {
	<T> Future<T> submitCommand(ICommand<T> command);
	<T> void submitCommand(ICommand<T> command,IAsyncCallback callback);
	ICommandExecutor registerCommandHandler(String cmdName,ICommandHandler handler);
	ICommandExecutor unregisterCommandHandler(String cmdName,ICommandHandler handler);
	ICommandExecutor registerCommandValidator(ICommandValidator validator);
	ICommandExecutor unregisterCommandValidator(ICommandValidator validator);
}
