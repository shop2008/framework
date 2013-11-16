/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.List;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.service.ITradingRecordManagementService;

/**
 * @author wangxuyang
 * 
 */
public class TradingRecordManagementServiceImpl extends AbstractModule<IStockAppContext>
		implements ITradingRecordManagementService{
	private static final Trace log = Trace.register(TradingRecordManagementServiceImpl.class);
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

	@Override
	public List<TradingRecordBean> getTradingAccountRecord(String acctID,
			int start, int limit) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
