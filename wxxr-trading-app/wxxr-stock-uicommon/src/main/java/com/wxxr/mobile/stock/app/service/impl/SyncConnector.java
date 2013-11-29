/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.service.IURLLocatorManagementService;
import com.wxxr.mobile.sync.client.impl.MTreeSyncConnector;

/**
 * @author wangxuyang
 * 
 */
public class SyncConnector extends MTreeSyncConnector<IStockAppContext> {

	@Override
	protected String getServerUrl() {
		return context.getService(IURLLocatorManagementService.class)
				.getServerURL();
	}

	@Override
	protected void initServiceDependency() {
		super.initServiceDependency();
		addRequiredService(IURLLocatorManagementService.class);
	}
}
