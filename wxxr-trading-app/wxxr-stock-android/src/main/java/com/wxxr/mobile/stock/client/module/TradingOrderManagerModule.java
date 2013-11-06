/**
 * 
 */
package com.wxxr.mobile.stock.client.module;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.stock.client.IStockAppContext;
import com.wxxr.mobile.stock.client.bean.Article;

/**
 * @author wangxuyang
 * 
 */
public class TradingOrderManagerModule extends AbstractModule<IStockAppContext>
		implements ITradingOrderManagerModule {
	private static final Trace log = Trace.register(TradingOrderManagerModule.class);
	// =================interface method =====================================


	// =================private method =======================================

	// =================module life cycle methods=============================

	@Override
	protected void initServiceDependency() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#startService()
	 */
	@Override
	protected void startService() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#stopService()
	 */
	@Override
	protected void stopService() {
		// TODO Auto-generated method stub

	}

}
