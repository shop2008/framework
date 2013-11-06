/**
 * 
 */
package com.wxxr.mobile.stock.client.module;

import java.util.List;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.stock.client.IStockAppContext;
import com.wxxr.mobile.stock.client.bean.TradingAccount;

/**
 * @author wangxuyang
 * 
 */
public class TradingManagerModule extends AbstractModule<IStockAppContext>
		implements ITradingManagerModule {

	private static final Trace log = Trace.register(TradingManagerModule.class);

	// =================interface method =====================================
	public List<TradingAccount> getTradingAccountList(int type){
		if (log.isDebugEnabled()) {
			log.debug(String.format("getTradingAccountList[type: %s]", type));
		}
		//从上下文取出当前用户
		String userId =(String)context.getAttribute("userId");
		return null;
	}

	public List<TradingAccount> getTradingAccountList(String userId, int type) {
		// TODO Auto-generated method stub
		return null;
	}
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
