/**
 * 
 */
package com.wxxr.mobile.stock.client.module;

import java.util.List;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.stock.client.IStockAppContext;
import com.wxxr.mobile.stock.client.bean.Stock;

/**
 * @author wangxuyang
 * 
 */
public class InfoCenterManagerModule extends AbstractModule<IStockAppContext>
		implements IInfoCenterManagerModule {

	private static final Trace log = Trace.register(InfoCenterManagerModule.class);
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
		context.registerService(IInfoCenterManagerModule.class, this);

	}


	@Override
	protected void stopService() {
		context.unregisterService(IInfoCenterManagerModule.class, this);
	}

}
