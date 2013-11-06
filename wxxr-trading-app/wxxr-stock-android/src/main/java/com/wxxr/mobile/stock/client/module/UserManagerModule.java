/**
 * 
 */
package com.wxxr.mobile.stock.client.module;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.stock.client.IStockAppContext;
import com.wxxr.mobile.stock.client.StockAppBizException;

/**
 * @author wangxuyang
 * 
 */
public class UserManagerModule extends AbstractModule<IStockAppContext>	implements IUserManagerModule {
	private static final Trace log = Trace.register(UserManagerModule.class);

	// =================interface method =====================================
	@Override
	public void login(String userId, String password) throws StockAppBizException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register(String userId, String password) throws StockAppBizException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logout(String userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetPassword(String userId, String oldPassword,String newPassword) throws StockAppBizException {
		// TODO Auto-generated method stub
		
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
