/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.HashMap;
import java.util.Map;

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
public class EntityLoaderRegistryImpl<T extends IKernelContext> extends AbstractModule<T> implements
		IEntityLoaderRegistry {
	
	private Map<String, IEntityLoader> registry  = new HashMap<String, IEntityLoader>();

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry#getEntityLoader(java.lang.String)
	 */
	@Override
	public IEntityLoader getEntityLoader(String entityName) {
		synchronized(this.registry){
			return this.registry.get(entityName);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry#registerEntityLoader(java.lang.String, com.wxxr.mobile.stock.app.common.IEntityLoader)
	 */
	@Override
	public IEntityLoaderRegistry registerEntityLoader(String entityName,
			IEntityLoader loader) {
		synchronized(this.registry){
			IEntityLoader old = this.registry.get(entityName);
			this.registry.put(entityName, loader);
			old.unregisterCommandHandler(context.getService(ICommandExecutor.class));
			if(old instanceof IKernelComponent){
				((IKernelComponent)old).destroy();
			}
			if(loader instanceof IKernelComponent){
				((IKernelComponent)loader).init(context);
			}
			loader.registerCommandHandler(context.getService(ICommandExecutor.class));
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry#unregisterEntityLoader(java.lang.String, com.wxxr.mobile.stock.app.common.IEntityLoader)
	 */
	@Override
	public IEntityLoaderRegistry unregisterEntityLoader(String entityName,
			IEntityLoader loader) {
		synchronized(this.registry){
			IEntityLoader old = this.registry.get(entityName);
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

}
