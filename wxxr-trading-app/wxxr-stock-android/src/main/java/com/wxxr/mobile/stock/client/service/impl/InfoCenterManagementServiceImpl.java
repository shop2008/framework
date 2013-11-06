/**
 * 
 */
package com.wxxr.mobile.stock.client.service.impl;

import java.util.List;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.stock.client.IStockAppContext;
import com.wxxr.mobile.stock.client.bean.Stock;
import com.wxxr.mobile.stock.client.service.IInfoCenterManagementService;

/**
 * @author wangxuyang
 * 
 */
public class InfoCenterManagementServiceImpl extends AbstractModule<IStockAppContext>
		implements IInfoCenterManagementService {

	private static final Trace log = Trace.register(InfoCenterManagementServiceImpl.class);
	//====================interface methods =====================
	@Override
	public List<Stock> searchStock(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

	// ====================module life cycle methods ==================
	@Override
	protected void initServiceDependency() {
		// TODO Auto-generated method stub

	}


	@Override
	protected void startService() {
		context.registerService(IInfoCenterManagementService.class, this);

	}


	@Override
	protected void stopService() {
		context.unregisterService(IInfoCenterManagementService.class, this);
	}

}
