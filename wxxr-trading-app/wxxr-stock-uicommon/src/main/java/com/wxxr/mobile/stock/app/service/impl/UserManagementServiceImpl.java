/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wxxr.javax.ws.rs.NotAuthorizedException;
import com.wxxr.mobile.core.api.IUserAuthCredential;
import com.wxxr.mobile.core.api.IUserAuthManager;
import com.wxxr.mobile.core.api.UsernamePasswordCredential;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.HttpRpcService;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.preference.api.IPreferenceManager;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.LoginFailedException;
import com.wxxr.mobile.stock.app.RestBizException;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.AuthInfoBean;
import com.wxxr.mobile.stock.app.bean.BindMobileBean;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.bean.ScoreBean;
import com.wxxr.mobile.stock.app.bean.ScoreInfoBean;
import com.wxxr.mobile.stock.app.bean.TradeDetailListBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountListBean;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.bean.VoucherBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.mock.MockDataUtils;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.app.service.handler.UpPwdHandler;
import com.wxxr.mobile.stock.app.service.handler.UpPwdHandler.UpPwdCommand;
import com.wxxr.mobile.stock.app.service.loader.RemindMessageLoader;
import com.wxxr.mobile.stock.app.service.loader.UserAssetLoader;
import com.wxxr.mobile.stock.app.service.loader.VoucherLoader;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.security.vo.BindMobileVO;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.stock.common.valobject.ResultBaseVO;
import com.wxxr.stock.crm.customizing.ejb.api.ActivityUserVo;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;
import com.wxxr.stock.notification.ejb.api.MessageVO;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.trading.ejb.api.GainVO;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;
import com.wxxr.stock.trading.ejb.api.PullMessageVO;
import com.wxxr.stock.trading.ejb.api.UserAssetVO;

/**
 * @author neillin
 * 
 */
public class UserManagementServiceImpl extends AbstractModule<IStockAppContext>
		implements IUserManagementService, IUserAuthManager {
	private static final Trace log = Trace
			.register(UserManagementServiceImpl.class);

//	private static final String KEY_USERNAME = "U";
//	private static final String KEY_PASSWORD = "P";
//	private static final String KEY_UPDATE_DATE = "UD";
	private IPreferenceManager prefManager;
	private UsernamePasswordCredential UsernamePasswordCredential4Login;
	// ==================beans =============================
	private UserBean myUserInfo ;
	private TradingAccountListBean myTradingAccountListBean = new TradingAccountListBean();
	private UserBean otherUserInfo = new UserBean();
	private BindMobileBean bindMobile = new BindMobileBean();
	private ScoreInfoBean myScoreInfo = new ScoreInfoBean();
	
	
	private TradeDetailListBean myTradeDetails = new TradeDetailListBean();
	/**
	 * 个人主页
	 */
	private PersonalHomePageBean myPBean = new PersonalHomePageBean();
	
	private UserAssetBean userAssetBean;
	
	private IReloadableEntityCache<String,UserAssetBean> userAssetBeanCache;
	
	
	private VoucherBean voucherBean;
	private IReloadableEntityCache<String,VoucherBean> voucherBeanCache;
	

	private BindableListWrapper<RemindMessageBean> remindMessageBean;
	private IReloadableEntityCache<String, RemindMessageBean> remindMessageBeanCache;
	
	private BindableListWrapper<PullMessageBean> pullMessageBean;
	private IReloadableEntityCache<Long, PullMessageBean> pullMessageBeanCache;

	
//	private BindableListWrapper<UserAtt>

	//==============  module life cycle =================
	@Override
	protected void initServiceDependency() {
		addRequiredService(IRestProxyService.class);
		addRequiredService(IEntityLoaderRegistry.class);
		addRequiredService(ICommandExecutor.class);
	}

	@Override
	protected void startService() {
		IEntityLoaderRegistry registry = getService(IEntityLoaderRegistry.class);
		registry.registerEntityLoader("userAssetBean", new UserAssetLoader());
		registry.registerEntityLoader("voucherBean", new VoucherLoader());
		registry.registerEntityLoader("remindMessageBean", new RemindMessageLoader());
		context.getService(ICommandExecutor.class).registerCommandHandler(UpPwdHandler.COMMAND_NAME, new UpPwdHandler());
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
//		IPreferenceManager mgr = getPrefManager();
//		if (!mgr.hasPreference(getModuleName())
//				|| mgr.getPreference(getModuleName()).get(KEY_USERNAME) == null) {
//			if (UsernamePasswordCredential4Login != null) {
				return UsernamePasswordCredential4Login;
//			}
//			
//			
//			IDialog dialog = getService(IWorkbenchManager.class).getWorkbench().createDialog("userLoginPage",null );
//			dialog.show();
//			
//		}
//		Dictionary<String, String> d = mgr.getPreference(getModuleName());
//		String userName = d.get(KEY_USERNAME);
//		String passwd = d.get(KEY_PASSWORD);
//		return new UsernamePasswordCredential(userName, passwd);
	}

	@Override
	public UserBean getMyUserInfo() {
		
//		if (context.getApplication().isInDebugMode()) {
//			myUserInfo = MockDataUtils.mockUserInfo();
//			return myUserInfo;
//		}
//		context.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					UserVO vo = context.getService(IRestProxyService.class)
//							.getRestService(StockUserResource.class).getUser();
//					if (vo != null) {
//						myUserInfo.setNickName(vo.getNickName());
//						myUserInfo.setUsername(vo.getUserName());
//						myUserInfo.setPhoneNumber(vo.getMoblie());
//						myUserInfo.setUserPic(vo.getIcon());
//					}else{
//						
//					}
//					
//				} catch (Exception e) {
//					log.warn("Error when get user info", e);
//				}
//			}
//		}, 1, TimeUnit.SECONDS);

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
	public void login(final String userId, final String pwd) throws LoginFailedException {
		Future<?> future = context.getExecutor().submit(new Runnable() {
			@Override
			public void run() {
				UsernamePasswordCredential4Login = new UsernamePasswordCredential(
						userId, pwd);
				try {
					UserVO vo = context.getService(IRestProxyService.class).getRestService(StockUserResource.class).getUser();
					myUserInfo = new UserBean();
					myUserInfo.setNickName(vo.getNickName());
					myUserInfo.setUsername(vo.getUserName());
					myUserInfo.setPhoneNumber(vo.getMoblie());
					myUserInfo.setUserPic(vo.getIcon());
				} catch (NotAuthorizedException e) {
					log.warn("Failed to login user due to invalid user name and/or password",e);
					UsernamePasswordCredential4Login = null;
					throw new LoginFailedException("用户名或密码错误");
				} catch (Throwable e) {
					log.warn("Failed to login user due to unexpected exception",e);
					UsernamePasswordCredential4Login = null;
					throw new LoginFailedException("登录失败，请稍后再试...");
				}
			}
		});
		if (future != null) {
			try {
				future.get(20, TimeUnit.SECONDS);
			} catch (ExecutionException e) {
				throw (StockAppBizException)e.getCause();
			} catch(Throwable e){
				log.warn("连接超时",e);
				throw new LoginFailedException("登录超时，请稍后再试...");
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
	public  void updatePassword(String oldPwd,String newPwd,String newPwd2) throws StockAppBizException {
		
		UpPwdCommand cmd=new UpPwdCommand();
		cmd.setOldPwd(oldPwd);
		cmd.setNewPwd(newPwd);
		cmd.setNewPwd2(newPwd2);
		Future<ResultBaseVO> future=context.getService(ICommandExecutor.class).submitCommand(cmd);
		try {
			ResultBaseVO vo=future.get(30,TimeUnit.SECONDS);
			if(vo.getResulttype()!=1){
				throw new StockAppBizException(vo.getResultInfo());
			}
		} catch (Exception e) {
			new StockAppBizException("系统错误");
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
		
		//getRestService(TradingResourse.class).get
		return null;
	}

	@Override
	public PersonalHomePageBean getOtherPersonalHomePage(final String userId) {
		PersonalHomePageBean otherPBean= null;
		/*PersonalHomePageVO vo = null;
		try {
			vo = fetchDataFromServer(new Callable<PersonalHomePageVO>() {
				public PersonalHomePageVO call() throws Exception {
					try {
						return getRestService(TradingResourse.class).getOtherHomeFromTDay(userId);
					} catch (Throwable e) {
						log.warn("Failed to fetch personal home page",e);
						throw new StockAppBizException("网络不给力，请稍后再试");
					}
				}
			});
		} catch (Exception e) {
			log.warn("Failed to fetch personal home page",e);
		}
		if (vo!=null) {
			otherPBean = new PersonalHomePageBean();
			otherPBean.setActualCount(vo.getActualCount());
			otherPBean.setVirtualCount(vo.getVirtualCount());
			otherPBean.setTotalProfit(vo.getTotalProfit());
			otherPBean.setVoucherVol(vo.getVoucherVol());
			List<GainVO> volist = vo.getActualList();
			if (volist!=null&&volist.size()>0) {
				List<GainBean> bean_list = new ArrayList<GainBean>(); 
				for (GainVO acVO : volist) {
					bean_list.add(ConverterUtils.fromVO(acVO));
				}
				otherPBean.setActualList(bean_list);
			}
			volist = vo.getVirtualList();
			if (volist!=null&&volist.size()>0) {
				List<GainBean> bean_list = new ArrayList<GainBean>(); 
				for (GainVO acVO : volist) {
					bean_list.add(ConverterUtils.fromVO(acVO));
				}
				otherPBean.setVirtualList(bean_list);
			}
		}*/
		return otherPBean;
	}
	
	
	@Override
	public PersonalHomePageBean getMyPersonalHomePage() {
		PersonalHomePageVO vo = null;
		try {
			vo = fetchDataFromServer(new Callable<PersonalHomePageVO>() {
				public PersonalHomePageVO call() throws Exception {
					try {
						return getRestService(ITradingProtectedResource.class).getSelfHomePage();
					} catch (Throwable e) {
						log.warn("Failed to fetch personal home page",e);
						throw new StockAppBizException("网络不给力，请稍后再试");
					}
				}
			});
		} catch (Exception e) {
			log.warn("Failed to fetch personal home page",e);
		}
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
		return myPBean;
	}
	@Override
	public PersonalHomePageBean getMorePersonalRecords(int start, int limit,
			boolean virtual) {
		PersonalHomePageBean otherPBean= null;
		/*PersonalHomePageVO vo = null;
		try {
			vo = fetchDataFromServer(new Callable<PersonalHomePageVO>() {
				public PersonalHomePageVO call() throws Exception {
					try {
						return getRestService(TradingResourse.class).getOtherHomeFromTDay(userId);
					} catch (Throwable e) {
						log.warn("Failed to fetch personal home page",e);
						throw new StockAppBizException("网络不给力，请稍后再试");
					}
				}
			});
		} catch (Exception e) {
			log.warn("Failed to fetch personal home page",e);
		}
		if (vo!=null) {
			otherPBean = new PersonalHomePageBean();
			otherPBean.setActualCount(vo.getActualCount());
			otherPBean.setVirtualCount(vo.getVirtualCount());
			otherPBean.setTotalProfit(vo.getTotalProfit());
			otherPBean.setVoucherVol(vo.getVoucherVol());
			List<GainVO> volist = vo.getActualList();
			if (volist!=null&&volist.size()>0) {
				List<GainBean> bean_list = new ArrayList<GainBean>(); 
				for (GainVO acVO : volist) {
					bean_list.add(ConverterUtils.fromVO(acVO));
				}
				otherPBean.setActualList(bean_list);
			}
			volist = vo.getVirtualList();
			if (volist!=null&&volist.size()>0) {
				List<GainBean> bean_list = new ArrayList<GainBean>(); 
				for (GainVO acVO : volist) {
					bean_list.add(ConverterUtils.fromVO(acVO));
				}
				otherPBean.setVirtualList(bean_list);
			}
		}*/
		return otherPBean;
	}

	@Override
	public PersonalHomePageBean getMoreOtherPersonal(String userId, int start,
			int limit, boolean virtual) {
		PersonalHomePageBean otherPBean= null;
		/*PersonalHomePageVO vo = null;
		try {
			vo = fetchDataFromServer(new Callable<PersonalHomePageVO>() {
				public PersonalHomePageVO call() throws Exception {
					try {
						return getRestService(TradingResourse.class).getOtherHomeFromTDay(userId);
					} catch (Throwable e) {
						log.warn("Failed to fetch personal home page",e);
						throw new StockAppBizException("网络不给力，请稍后再试");
					}
				}
			});
		} catch (Exception e) {
			log.warn("Failed to fetch personal home page",e);
		}
		if (vo!=null) {
			otherPBean = new PersonalHomePageBean();
			otherPBean.setActualCount(vo.getActualCount());
			otherPBean.setVirtualCount(vo.getVirtualCount());
			otherPBean.setTotalProfit(vo.getTotalProfit());
			otherPBean.setVoucherVol(vo.getVoucherVol());
			List<GainVO> volist = vo.getActualList();
			if (volist!=null&&volist.size()>0) {
				List<GainBean> bean_list = new ArrayList<GainBean>(); 
				for (GainVO acVO : volist) {
					bean_list.add(ConverterUtils.fromVO(acVO));
				}
				otherPBean.setActualList(bean_list);
			}
			volist = vo.getVirtualList();
			if (volist!=null&&volist.size()>0) {
				List<GainBean> bean_list = new ArrayList<GainBean>(); 
				for (GainVO acVO : volist) {
					bean_list.add(ConverterUtils.fromVO(acVO));
				}
				otherPBean.setVirtualList(bean_list);
			}
		}*/
		return otherPBean;
	}
	private <T> T fetchDataFromServer(Callable<T> task) throws Exception{
		Future<T> future = context.getExecutor().submit(task);
		T result = null;
		try {
			result = future.get();
			return result;
		} catch (Exception e) {
			log.warn("Error when fetching data from server", e);
			throw e;
		}
	}

	private <T> T getRestService(Class<T> restResouce) {
		return context.getService(IRestProxyService.class).getRestService(
				restResouce);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getUserAssetBean()
	 */
	@Override
	public UserAssetBean getUserAssetBean() {
		if(userAssetBean==null){
			if(userAssetBeanCache==null){
				userAssetBeanCache=new GenericReloadableEntityCache<String, UserAssetBean, UserAssetVO>("userAssetBean");
			}
			userAssetBean=userAssetBeanCache.getEntity(UserAssetBean.class.getCanonicalName());
			if(userAssetBean==null){
				userAssetBean=new UserAssetBean();
				userAssetBeanCache.putEntity(UserAssetBean.class.getCanonicalName(), userAssetBean);
			}
		}
		userAssetBeanCache.doReloadIfNeccessay();
		return userAssetBean;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getVoucherBean()
	 */
	@Override
	public VoucherBean getVoucherBean() {
		if(voucherBean==null){
			if(voucherBeanCache==null){
				voucherBeanCache=new GenericReloadableEntityCache<String, VoucherBean, ActivityUserVo>("voucherBean");
			}
			voucherBean=voucherBeanCache.getEntity(VoucherBean.class.getCanonicalName());
			if(voucherBean==null){
				voucherBean=new VoucherBean();
				voucherBeanCache.putEntity(VoucherBean.class.getCanonicalName(), voucherBean);
			}
		}
		voucherBeanCache.doReloadIfNeccessay();
		return voucherBean;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getRemindMessageBean()
	 */
	@Override
	public BindableListWrapper<RemindMessageBean> getRemindMessageBean() {
		if(remindMessageBean==null){
			if(remindMessageBeanCache==null){
				remindMessageBeanCache=new GenericReloadableEntityCache<String, RemindMessageBean, MessageVO>("remindMessageBean");
			}
			remindMessageBean=remindMessageBeanCache.getEntities(null, null);
		}
		remindMessageBeanCache.doReloadIfNeccessay();
		return remindMessageBean;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getPullMessageBean()
	 */
	@Override
	public BindableListWrapper<PullMessageBean> getPullMessageBean(int start,int limit) {
		
		if(pullMessageBean==null){
			if(pullMessageBeanCache==null){
				pullMessageBeanCache=new GenericReloadableEntityCache<Long, PullMessageBean, PullMessageVO>("pullMessageBean");
			}
			pullMessageBean=pullMessageBeanCache.getEntities(null, null);
		}
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("start", start);
		params.put("limit", limit);
		pullMessageBeanCache.doReloadIfNeccessay(params);
		return pullMessageBean;
	}


	
	
}
