/**
 * 
 */
package com.wxxr.mobile.stock.client.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.stock.client.IStockAppContext;
import com.wxxr.mobile.stock.client.bean.TradingAccount;
import com.wxxr.mobile.stock.client.bean.User;
import com.wxxr.mobile.stock.client.bean.UserInfoEntity;
import com.wxxr.mobile.stock.client.service.IUserManagementService;

/**
 * @author neillin
 *
 */
public class UserManagementServiceImpl extends AbstractModule<IStockAppContext> implements
		IUserManagementService {
	private static final Trace log = Trace.register(UserManagementServiceImpl.class);

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
	private UserInfoEntity entity;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.client.service.IUserManagementService#getMyInfo()
	 */
	@Override
	public UserInfoEntity getMyInfo() {
//		if(entity == null){
//			context.invokeLater(new Runnable() {
//				
//				@Override
//				public void run() {
//					UserInfoEntity info = new UserInfoEntity();
//					info.setNickName("江泽明");
//					info.setPhoneNumber("13812232123");
//					entity = info;
//				}
//			}, 10, TimeUnit.SECONDS);
//		}
		return entity;
	}

	@Override
	public User fetchUserInfo() {
		User user = new User();
	
		user.setNickName("王五");
		user.setUserPic("resourceId:drawable/head1");
		user.setBalance("1000");
		user.setScore("20");
		user.setUsername("李四");
		user.setTotoalProfit("20000.00");
		user.setTotoalScore("12000.00");
		user.setChallengeShared("0");
		user.setJoinShared("19");
		
		List<TradingAccount> tradeInfos = new ArrayList<TradingAccount>();
		for(int i=0;i<5;i++) {
			TradingAccount account = new TradingAccount();
			account.setIncome(i%2==0?2000.00f:-100.00f);
			account.setCreateDate("2012-10-2");
			account.setType(i%2==0? 1: 0);
			account.setStockName("股票"+i);
			account.setStockCode("60020");
			account.setStatus(i%2==0?0:1);
			account.setAvailable(2000.0f);
			tradeInfos.add(account);
		}
		
		user.setTradeInfos(tradeInfos);
		return user;
	}
	@Override
	public void register(String userId) {
		log.info("userId:"+userId);
		
	}

	@Override
	public void login(String userId, String pwd) {
		log.info("userId:"+userId+",pwd:"+pwd);
		
	}

	@Override
	public void pushMessageSetting(boolean on) {
		
		
	}

	@Override
	public boolean getPushMessageSetting() {
		// TODO Auto-generated method stub
		return false;
	}

}
