/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wxxr.javax.ws.rs.NotAuthorizedException;
import com.wxxr.mobile.android.preference.DictionaryUtils;
import com.wxxr.mobile.core.api.IUserAuthCredential;
import com.wxxr.mobile.core.api.IUserAuthManager;
import com.wxxr.mobile.core.api.UsernamePasswordCredential;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.HttpRpcService;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.ui.api.IDialog;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.preference.api.IPreferenceManager;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.RestBizException;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.AuthInfoBean;
import com.wxxr.mobile.stock.app.bean.BindMobileBean;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.bean.ScoreBean;
import com.wxxr.mobile.stock.app.bean.ScoreInfoBean;
import com.wxxr.mobile.stock.app.bean.TradeDetailListBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountListBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.mock.MockDataUtils;
import com.wxxr.mobile.stock.app.model.UserLoginCallback;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.security.vo.BindMobileVO;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.security.vo.UpdatePwdVO;
import com.wxxr.security.vo.UserBaseInfoVO;
import com.wxxr.stock.common.valobject.ResultBaseVO;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.restful.resource.TradingResourse;
import com.wxxr.stock.trading.ejb.api.GainVO;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;

/**
 * @author neillin
 * 
 */
public class UserManagementServiceImpl extends AbstractModule<IStockAppContext>
		implements IUserManagementService, IUserAuthManager {
	private static final Trace log = Trace
			.register(UserManagementServiceImpl.class);

	private static final String KEY_USERNAME = "U";
	private static final String KEY_PASSWORD = "P";
	private static final String KEY_UPDATE_DATE = "UD";
	private IPreferenceManager prefManager;
	private UsernamePasswordCredential UsernamePasswordCredential4Login;
	// ==================beans =============================
	private UserBean myUserInfo = new UserBean();
	private TradingAccountListBean myTradingAccountListBean = new TradingAccountListBean();
	private UserBean otherUserInfo = new UserBean();
	private BindMobileBean bindMobile = new BindMobileBean();
	private UserLoginCallback loginCallback;
	private ScoreInfoBean myScoreInfo = new ScoreInfoBean();
	
	private PersonalHomePageBean myPersonalHomePageBean = new PersonalHomePageBean();
	private TradeDetailListBean myTradeDetails = new TradeDetailListBean();
	/**
	 * 他人主页
	 */
	private PersonalHomePageBean otherPBean = new PersonalHomePageBean();
	/**
	 * 个人主页
	 */
	private PersonalHomePageBean myPBean = new PersonalHomePageBean();
	//==============  module life cycle =================
	@Override
	protected void initServiceDependency() {
		addRequiredService(IRestProxyService.class);
	}

	@Override
	protected void startService() {
		context.registerService(IUserManagementService.class, this);
		context.registerService(IUserAuthManager.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(IUserManagementService.class, this);
		context.unregisterService(IUserAuthManager.class, this);
	}

	protected IPreferenceManager getPrefManager() {
		if (this.prefManager == null) {
			this.prefManager = context.getService(IPreferenceManager.class);
		}
		return this.prefManager;
	}

	@Override
	public IUserAuthCredential getAuthCredential(String host, String realm) {
		IPreferenceManager mgr = getPrefManager();
		if (!mgr.hasPreference(getModuleName())
				|| mgr.getPreference(getModuleName()).get(KEY_USERNAME) == null) {
			if (UsernamePasswordCredential4Login != null) {
				return UsernamePasswordCredential4Login;
			}
			
			
			IDialog dialog = getService(IWorkbenchManager.class).getWorkbench().createDialog("userLoginPage",null );
			dialog.show();
			
		}
		Dictionary<String, String> d = mgr.getPreference(getModuleName());
		String userName = d.get(KEY_USERNAME);
		String passwd = d.get(KEY_PASSWORD);
		return new UsernamePasswordCredential(userName, passwd);
	}

	@Override
	public UserBean getMyUserInfo() {
		
		if (context.getApplication().isInDebugMode()) {
			myUserInfo = MockDataUtils.mockUserInfo();
			return myUserInfo;
		}
		context.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UserVO vo = context.getService(IRestProxyService.class)
							.getRestService(StockUserResource.class).getUser();
					if (vo != null) {
						myUserInfo.setNickName(vo.getNickName());
						myUserInfo.setUsername(vo.getUserName());
						myUserInfo.setPhoneNumber(vo.getMoblie());
						myUserInfo.setUserPic(vo.getIcon());
					}else{
						
					}
					
				} catch (Exception e) {
					log.warn("Error when get user info", e);
				}
			}
		}, 1, TimeUnit.SECONDS);

		return myUserInfo;
	}

	@Override
	public void register(final String phoneNumber) throws StockAppBizException {
		Future<StockAppBizException> future = context.getExecutor().submit(
				new Callable<StockAppBizException>() {
					@Override
					public StockAppBizException call() throws Exception {
						try {
							SimpleResultVo vo = context
									.getService(IRestProxyService.class)
									.getRestService(StockUserResource.class)
									.register(phoneNumber);
							if (vo != null && vo.getResult() == 0) {
								return null;
							}
							return new StockAppBizException("注册失败");
						} catch (RestBizException e) {
							return new StockAppBizException("用户已存在");
						}
					}
				});
		if (future != null) {
			try {
				StockAppBizException e = future.get(1, TimeUnit.SECONDS);
				if (e != null) {
					if (log.isDebugEnabled()) {
						log.debug("Register error", e);
					}
					throw e;
				}
			} catch (Exception e) {
				throw new StockAppBizException("网络连接超时，请稍后再试");
			}
		}
	}

	@Override
	public void login(final String userId, final String pwd) throws StockAppBizException {
		Callable<StockAppBizException> task = new Callable<StockAppBizException>() {
			public StockAppBizException call() throws Exception {
				UserBaseInfoVO vo = null;
				UsernamePasswordCredential4Login = new UsernamePasswordCredential(
						userId, pwd);
				try {
					vo = context.getService(IRestProxyService.class)
							.getRestService(StockUserResource.class).info();
				} catch (NotAuthorizedException e) {
					log.warn("用户名或密码错误",e);
					return new StockAppBizException("用户名或密码错误");
				} finally {
					UsernamePasswordCredential4Login = null;
				}
				if (vo == null) {//未登录成功，弹出登陆对话框
					getService(IWorkbenchManager.class).getWorkbench().showPage("userLoginPage", null, null);
				}
				
				myUserInfo.setLogin(true);
				// 根据用户密码登录成功
				Dictionary<String, String> pref = getPrefManager()
						.getPreference(getModuleName());
				if (pref == null) {
					pref = new Hashtable<String, String>();
					getPrefManager().newPreference(getModuleName(), pref);
				} else {
					pref = DictionaryUtils.clone(pref);
				}
				pref.put(KEY_USERNAME, userId);
				pref.put(KEY_PASSWORD, pwd);
				pref.put(KEY_UPDATE_DATE,
						String.valueOf(System.currentTimeMillis()));
				getPrefManager().putPreference(getModuleName(), pref);
				return null;
			}
		};
		Future<StockAppBizException> future = context.getExecutor().submit(task);
		if (future != null) {
			try {
				StockAppBizException e = future.get(7, TimeUnit.SECONDS);
				if (e != null) {
					if (log.isDebugEnabled()) {
						log.debug("Login error", e);
					}
					throw e;
				}
			} catch (Exception e) {
				log.warn("连接超时",e);
			}
		}

	}

	@Override
	public String getModuleName() {
		return "UserManagementService";
	}

	@Override
	public void pushMessageSetting(boolean on) {

	}

	@Override
	public boolean getPushMessageSetting() {
		return false;
	}

	public void setRegRulesReaded(boolean isRead) {

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
		for (int i = 0; i < 9; i++) {
			ScoreBean score = new ScoreBean();
			score.setCatagory("推荐好友奖励");
			score.setAmount(200f);
			score.setDate("2011-10-12");
			scores.add(score);
		}

		entity.setScores(scores);
		return entity;
	}

	@Override
	public void updatePassword(final String oldPwd, final String newPwd)
			throws StockAppBizException {
		Future<StockAppBizException> future = context.getExecutor().submit(
				new Callable<StockAppBizException>() {
					@Override
					public StockAppBizException call() throws Exception {
						try {
							UpdatePwdVO vo = new UpdatePwdVO();
							vo.setOldPwd(oldPwd);
							vo.setPassword(newPwd);
							ResultBaseVO ret = context
									.getService(IRestProxyService.class)
									.getRestService(StockUserResource.class)
									.updatePwd(vo);
							if (ret != null && ret.getResulttype() == 0) {
								if (ret.getResulttype() != 0) {
									return new StockAppBizException(ret
											.getResultInfo());
								}
								return null;
							}
							return new StockAppBizException("更新失败");
						} catch (RestBizException e) {
							return new StockAppBizException("更新失败");
						}
					}
				});
		if (future != null) {
			try {
				StockAppBizException e = future.get(1, TimeUnit.SECONDS);
				if (e != null) {
					if (log.isDebugEnabled()) {
						log.debug("Register error", e);
					}
					throw e;
				}
			} catch (Exception e) {
				throw new StockAppBizException("网络连接超时，请稍后再试");
			}
		}

	}

	@Override
	public void checkLogin() {
		getMyUserInfo();
	}

	@Override
	public void register(String userId, String password)
			throws StockAppBizException {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindMobile(final String phoneNumber, final String vertifyCode)
			throws StockAppBizException {
		Future<StockAppBizException> future = context.getExecutor().submit(
				new Callable<StockAppBizException>() {
					@Override
					public StockAppBizException call() throws Exception {
						try {
							BindMobileVO vo = new BindMobileVO();
							vo.setMobileNum(phoneNumber);
							vo.setCode(vertifyCode);
							ResultBaseVO ret = context
									.getService(IRestProxyService.class)
									.getRestService(StockUserResource.class)
									.bindMobile(vo);
							if (ret != null && ret.getResulttype() == 0) {
								if (ret.getResulttype() != 0) {
									return new StockAppBizException(ret
											.getResultInfo());
								}
								return null;
							}
							return new StockAppBizException("更新失败");
						} catch (RestBizException e) {
							return new StockAppBizException("更新失败");
						}
					}
				});
		if (future != null) {
			try {
				StockAppBizException e = future.get(1, TimeUnit.SECONDS);
				if (e != null) {
					if (log.isDebugEnabled()) {
						log.debug("Register error", e);
					}
					throw e;
				}
			} catch (Exception e) {
				throw new StockAppBizException("网络连接超时，请稍后再试");
			}
		}
	}

	@Override
	public void changeBindingMobile(String phoneNumber, String vertifyCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public UserBean getUserInfoById(String userId) {
		context.invokeLater(new Runnable() {
			public void run() {
				
				
			}
		}, 1, TimeUnit.SECONDS);
		return otherUserInfo;
	}

	public boolean switchBankCard(String accountName, String bankName,
			String bankAddr, String bankNum) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean withDrawCashAuth(String accountName, String bankName,
			String bankAddr, String bankNum) {
		// TODO Auto-generated method stub
		return false;
	}

	public AuthInfoBean getUserAuthInfo() {

		return null;
	}

	public String getUserAuthMobileNum(String userId) {

		return null;
	}

	@Override
	public void logout(){
		context.invokeLater(new Runnable() {
			@Override
			public void run() {
				HttpRpcService httpService = context.getService(HttpRpcService.class);
				if (httpService != null) {
					httpService.resetHttpClientContext();
				}
			}
		}, 1, TimeUnit.SECONDS);
	}

	@Override
	public ScoreInfoBean getMyUserScoreInfo() {
		if (context.getApplication().isInDebugMode()) {
			
			myScoreInfo = MockDataUtils.mockScoreInfo();
			return myScoreInfo;
		}
		return null;
	}

	@Override
	public TradeDetailListBean getMyTradeDetailInfo() {
		if (context.getApplication().isInDebugMode()) {
			myTradeDetails = MockDataUtils.mockTradeDetails();
			return myTradeDetails;
		}
		return null;
	}

	@Override
	public PersonalHomePageBean getOtherPersonalHomePage(String userId) {
		context.invokeLater(new Runnable() {
			public void run() {
				//getService(IRestProxyService.class).getRestService(TradingResourse.class)
				
			}
		}, 1, TimeUnit.SECONDS);
		return otherPBean;
	}
	
	
	@Override
	public PersonalHomePageBean getMyPersonalHomePage() {
		
		if (context.getApplication().isInDebugMode()) {
			myPersonalHomePageBean = MockDataUtils.mockPersonalHome();
			return myPersonalHomePageBean;
		}
		context.invokeLater(new Runnable() {
			public void run() {
				try {
					PersonalHomePageVO vo = getRestService(TradingResourse.class).getSelfHomePage();
					if (vo!=null) {
						myPBean.setActualCount(vo.getActualCount());
						myPBean.setVirtualCount(vo.getVirtualCount());
						myPBean.setTotalProfit(vo.getTotalProfit());
						myPBean.setVoucherVol(vo.getVoucherVol());
						List<GainVO> volist = vo.getActualList();
						if (volist!=null&&volist.size()>0) {
							List<GainBean> bean_list = new ArrayList<GainBean>(); 
							for (GainVO acVO : volist) {
								bean_list.add(ConverterUtils.fromVO(acVO));
							}
							myPBean.setActualList(bean_list);
						}
						volist = vo.getVirtualList();
						if (volist!=null&&volist.size()>0) {
							List<GainBean> bean_list = new ArrayList<GainBean>(); 
							for (GainVO acVO : volist) {
								bean_list.add(ConverterUtils.fromVO(acVO));
							}
							myPBean.setVirtualList(bean_list);
						}
					}
					
				} catch (Throwable e) {
					log.warn("Failed to fetch personal home page",e);
					throw new StockAppBizException("网络不给力，请稍后再试");
				}
				
			}
		}, 1, TimeUnit.SECONDS);
		return myPBean;
	}
	
	private <T> T getRestService(Class<T> restResouce){
		return getService(IRestProxyService.class).getRestService(restResouce);
	}

	@Override
	public UserLoginCallback createLoginCallback() {
		this.loginCallback = new UserLoginCallback();
		return this.loginCallback;
	}

	@Override
	public PersonalHomePageBean getMorePersonalRecords(int start, int limit,
			boolean virtual) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PersonalHomePageBean getMoreOtherPersonal(String userId, int start,
			int limit, boolean virtual) {
		// TODO Auto-generated method stub
		return null;
	}

}
