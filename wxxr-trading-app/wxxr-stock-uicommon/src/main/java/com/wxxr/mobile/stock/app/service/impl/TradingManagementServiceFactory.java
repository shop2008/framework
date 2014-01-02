/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import com.wxxr.mobile.core.microkernel.api.AbstractStatefulServiceFactory;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.microkernel.api.IStatefulService;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.service.ITradingManagementServiceFactory;

/**
 * @author wangyan
 *
 */
public class TradingManagementServiceFactory  extends AbstractStatefulServiceFactory<IStockAppContext> implements ITradingManagementServiceFactory {


	@Override
	protected void initServiceDependency() {
		addRequiredService(IRestProxyService.class);
		addRequiredService(IEntityLoaderRegistry.class);
	    addRequiredService(IUserIdentityManager.class);
	}

	@Override
	protected void startService() {
		context.registerService(ITradingManagementServiceFactory.class, this);

	}

	@Override
	protected void stopService() {
		context.unregisterService(ITradingManagementServiceFactory.class, this);
	}


	@Override
	protected IStockAppContext getContext() {
		return context;
	}

	@Override
	protected IStatefulService createStatefulService() {
		return new TradingManagementServiceImpl();
	}

}
