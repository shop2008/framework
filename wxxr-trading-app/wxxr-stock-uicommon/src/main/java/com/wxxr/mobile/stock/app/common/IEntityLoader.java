/**
 * 
 */
package com.wxxr.mobile.stock.app.common;

import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutor;

/**
 * @author neillin
 *
 */
public interface IEntityLoader {
	ICommand<?> createCommand(Map<String, Object> params);
	boolean handleCommandResult(Object result, IReloadableEntityCache<?, ?> cache);
	void registerCommandHandler(ICommandExecutor executor);
	void unregisterCommandHandler(ICommandExecutor executor);
}
