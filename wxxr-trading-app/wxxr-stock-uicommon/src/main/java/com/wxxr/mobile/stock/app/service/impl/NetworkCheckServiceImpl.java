/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import com.wxxr.mobile.core.api.IDataExchangeCoordinator;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.service.INetworkCheckService;

/**
 * @author wangxuyang
 * 
 */
public class NetworkCheckServiceImpl extends AbstractModule<IStockAppContext>
		implements INetworkCheckService {
	private static final Trace log = Trace.register(NetworkCheckServiceImpl.class);
	@Override
	public boolean isNetworkConnected() {
		return getService(IDataExchangeCoordinator.class)
				.checkAvailableNetwork() > 0;
	}

	@Override
	public boolean isWifiConnected() {
		return getService(IDataExchangeCoordinator.class)
				.checkAvailableNetwork() == IDataExchangeCoordinator.NETWORK_ID_WIFI;
	}

	public boolean is3GConnected() {
		return getService(IDataExchangeCoordinator.class)
				.checkAvailableNetwork() == IDataExchangeCoordinator.NETWORK_ID_3G;
	}

	public boolean isGSMConnected() {
		return getService(IDataExchangeCoordinator.class)
				.checkAvailableNetwork() == IDataExchangeCoordinator.NETWORK_ID_GSM;
	}

	@Override
	protected void initServiceDependency() {
		addRequiredService(IDataExchangeCoordinator.class);
	}

	@Override
	protected void startService() {
		context.registerService(INetworkCheckService.class, this);

	}

	@Override
	protected void stopService() {
		context.unregisterService(INetworkCheckService.class, this);
	}

}
