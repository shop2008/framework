package com.wxxr.mobile.core.command.api;

/**
 * @author neillin
 *
 */
public interface ICommandHandler {
   <T> T execute(ICommand<T> command) throws Exception;
   void init(ICommandExecutionContext ctx);
   void destroy();

}