/**
 * 
 */
package com.wxxr.mobile.stock.app.common;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutor;

/**
 * @author neillin
 *
 */
public interface IEntityLoader<K,V,T> {
	ICommand<List<T>> createCommand(Map<String, Object> params);
	boolean handleCommandResult(ICommand<?> cmd,List<T> result, IReloadableEntityCache<K, V> cache);
	void registerCommandHandler(ICommandExecutor executor);
	void unregisterCommandHandler(ICommandExecutor executor);
}
