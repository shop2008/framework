package com.wxxr.mobile.core.command.api;

import com.wxxr.mobile.core.async.api.IAsyncCallback;

/**
 * @author neillin
 *
 */
public interface ICommandHandler<T, C extends ICommand<T>> {
   void execute(C command,IAsyncCallback<T> callback);
   void init(ICommandExecutionContext ctx);
   void destroy();

}