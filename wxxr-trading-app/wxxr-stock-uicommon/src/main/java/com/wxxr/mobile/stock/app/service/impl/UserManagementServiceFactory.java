/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.microkernel.api.AbstractStatefulServiceFactory;
import com.wxxr.mobile.core.microkernel.api.IStatefulService;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.preference.api.IPreferenceManager;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.service.IUserManagementServiceFactory;

/**
 * @author wangyan
 *
 */
public class UserManagementServiceFactory extends AbstractStatefulServiceFactory<IStockAppContext> implements
		IUserManagementServiceFactory {



	@Override
	protected IStockAppContext getContext() {
		return context;
	}

	@Override
	protected IStatefulService createStatefulService() {
		return new UserManagementServiceImpl();
	}

	@Override
	protected void initServiceDependency() {
		addRequiredService(IRestProxyService.class);
		addRequiredService(IEntityLoaderRegistry.class);
		addRequiredService(ICommandExecutor.class);
	    addRequiredService(IPreferenceManager.class);
	    addRequiredService(IUserIdentityManager.class);
	}

	@Override
	protected void startService() {
		this.context.registerService(IUserManagementServiceFactory.class, this);
	}

	@Override
	protected void stopService() {
		this.context.unregisterService(IUserManagementServiceFactory.class, this);
	}

}
