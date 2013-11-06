/**
 * 
 */
package com.wxxr.mobile.stock.client.service.impl;

import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.stock.client.IStockAppContext;
import com.wxxr.mobile.stock.client.bean.UserInfoEntity;
import com.wxxr.mobile.stock.client.service.IUserManagementService;

/**
 * @author neillin
 *
 */
public class UserManagementServiceImpl extends AbstractModule<IStockAppContext> implements
		IUserManagementService {

	private UserInfoEntity entity;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.client.service.IUserManagementService#getMyInfo()
	 */
	@Override
	public UserInfoEntity getMyInfo() {
		if(entity == null){
			context.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					UserInfoEntity info = new UserInfoEntity();
					info.setNickName("江泽明");
					info.setPhoneNumber("13812232123");
					entity = info;
				}
			}, 10, TimeUnit.SECONDS);
		}
		return entity;
	}

	@Override
	protected void initServiceDependency() {
		
	}

	@Override
	protected void startService() {
		context.registerService(IUserManagementService.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(IUserManagementService.class, this);
		
	}

}
