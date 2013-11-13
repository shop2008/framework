/**
 * 
 */
package com.wxxr.mobile.stock.client.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.preference.api.IPreferenceManager;
import com.wxxr.mobile.stock.client.IStockAppContext;
import com.wxxr.mobile.stock.client.bean.ScoreBean;
import com.wxxr.mobile.stock.client.bean.ScoreInfoBean;
import com.wxxr.mobile.stock.client.bean.TradingAccountBean;
import com.wxxr.mobile.stock.client.bean.UserBean;
import com.wxxr.mobile.stock.client.service.IUserManagementService;
import com.wxxr.mobile.stock.security.impl.Connector;
import com.wxxr.mobile.stock.security.impl.ConnectorContext;

/**
 * @author neillin
 *
 */
public class UserManagementServiceImpl extends AbstractModule<IStockAppContext> implements
		IUserManagementService {
	private static final Trace log = Trace.register(UserManagementServiceImpl.class);

	@Override
	protected void initServiceDependency() {
		//addRequiredService(IPreferenceManager.class);
	}

	@Override
	protected void startService() {
		ConnectorContext conectorContext = new ConnectorContext(context);
		Connector.createConnector(conectorContext);
		context.registerService(IUserManagementService.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(IUserManagementService.class, this);
		
	}
	
	
	@Override
	public UserBean fetchUserInfo() {
		UserBean user = new UserBean();
	
		user.setNickName("王五");
		user.setUserPic("resourceId:drawable/head1");
		user.setBalance("1000");
		user.setScore("20");
		user.setUsername("李四");
		user.setTotoalProfit("20000.00");
		user.setTotoalScore("12000.00");
		user.setChallengeShared("0");
		user.setJoinShared("19");
		
		List<TradingAccountBean> tradeInfos = new ArrayList<TradingAccountBean>();
		for(int i=0;i<5;i++) {
			TradingAccountBean account = new TradingAccountBean();
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
		final String userName = userId;
		final String password = pwd;
		context.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Connector.getInstance().login(userName, password);
				} catch (LoginException e) {
					log.warn("Login Failed", e);
				}
			}
		}, 1, TimeUnit.SECONDS);
		
	}

	@Override
	public void pushMessageSetting(boolean on) {
		
		
	}

	@Override
	public boolean getPushMessageSetting() {
		return false;
	}

	public void setRegRulesReaded(boolean isRead) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isBindCard() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean bindBankCard(String accountName, String bankName,
			String bankAddr, String bankNum) {
		// TODO Auto-generated method stub
		return false;
	}

	public ScoreInfoBean fetchUserScoreInfo(String userId) {
		
		ScoreInfoBean entity = new ScoreInfoBean();
		
		entity.setBalance("200");
		
		List<ScoreBean> scores = new ArrayList<ScoreBean>();
		for(int i=0;i<9;i++) {
			ScoreBean score = new ScoreBean();
			score.setCatagory("推荐好友奖励");
			score.setAmount("200");
			score.setDate("2011-10-12");
			scores.add(score);
		}
		
		entity.setScores(scores);
		return entity;
	}

	

}
