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
public interface IEntityLoader<K,V,T> {
	ICommand<T> createCommand(Map<String, Object> params);
	boolean handleCommandResult(T result, IReloadableEntityCache<K, V> cache);
	void registerCommandHandler(ICommandExecutor executor);
	void unregisterCommandHandler(ICommandExecutor executor);
}
