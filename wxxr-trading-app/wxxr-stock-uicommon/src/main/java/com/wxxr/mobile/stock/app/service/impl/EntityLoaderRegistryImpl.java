/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.microkernel.api.IKernelComponent;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.stock.app.common.IEntityLoader;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;

/**
 * @author neillin
 *
 */
public class EntityLoaderRegistryImpl<KC extends IKernelContext> extends AbstractModule<KC> implements
		IEntityLoaderRegistry {
	
	private Map<String, IEntityLoader<?,?,?,?>> registry  = new HashMap<String, IEntityLoader<?,?,?,?>>();


	@Override
	protected void initServiceDependency() {
		addRequiredService(ICommandExecutor.class);
	}

	@Override
	protected void startService() {
		context.registerService(IEntityLoaderRegistry.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(IEntityLoaderRegistry.class, this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IEntityLoader<?, ?, ?, ?> getEntityLoader(
			String entityName) {
		synchronized(this.registry){
			@SuppressWarnings("rawtypes")
			IEntityLoader loader = this.registry.get(entityName);
			return loader;
		}
	}

	@Override
	public <K, V, T, C extends ICommand<List<T>>> IEntityLoaderRegistry registerEntityLoader(
			String entityName, IEntityLoader<K, V, T, C> loader) {
		synchronized(this.registry){
			IEntityLoader<?,?,?,?> old = this.registry.get(entityName);
			this.registry.put(entityName, loader);
			if(old != null){
				old.unregisterCommandHandler(context.getService(ICommandExecutor.class));
				if(old instanceof IKernelComponent){
					((IKernelComponent)old).destroy();
				}
			}
			if(loader instanceof IKernelComponent){
				((IKernelComponent)loader).init(context);
			}
			loader.registerCommandHandler(context.getService(ICommandExecutor.class));
		}
		return this;
	}

	@Override
	public <K, V, T, C extends ICommand<List<T>>> IEntityLoaderRegistry unregisterEntityLoader(
			String entityName, IEntityLoader<K, V, T, C> loader) {
		synchronized(this.registry){
			IEntityLoader<?,?,?,?> old = this.registry.get(entityName);
			if(old == loader){
				this.registry.remove(entityName);
				loader.unregisterCommandHandler(context.getService(ICommandExecutor.class));
				if(old instanceof IKernelComponent){
					((IKernelComponent)old).destroy();
				}
			}
		}
		return this;
	}

}
