/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.AsyncFuture;
import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.core.async.api.IDataConverter;
import com.wxxr.mobile.core.async.api.NestedRuntimeException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.microkernel.api.IServiceDecoratorBuilder;
import com.wxxr.mobile.core.microkernel.api.IServiceDelegateHolder;
import com.wxxr.mobile.core.microkernel.api.IStatefulService;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.session.api.ISessionManager;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.preference.api.IPreferenceManager;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.ClientInfoBean;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;
import com.wxxr.mobile.stock.app.bean.GuideGainBean;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.bean.SearchUserListBean;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.bean.UserSignBean;
import com.wxxr.mobile.stock.app.bean.UserWrapper;
import com.wxxr.mobile.stock.app.bean.VoucherBean;
import com.wxxr.mobile.stock.app.command.CheckGuideGainCommand;
import com.wxxr.mobile.stock.app.command.GetGuideGainCommand;
import com.wxxr.mobile.stock.app.command.GetGuideGainRuleCommand;
import com.wxxr.mobile.stock.app.command.GetUserSignMessageCommand;
import com.wxxr.mobile.stock.app.command.SearchUserCommand;
import com.wxxr.mobile.stock.app.command.SignUpCommand;
import com.wxxr.mobile.stock.app.common.AsyncUtils;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.IBindableEntityCache;
import com.wxxr.mobile.stock.app.common.IEntityFetcher;
import com.wxxr.mobile.stock.app.common.IEntityFilter;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.model.AuthInfo;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.IUserLoginManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.app.service.handler.CheckGuideGainHandler;
import com.wxxr.mobile.stock.app.service.handler.GetGuideGainHandler;
import com.wxxr.mobile.stock.app.service.handler.GetGuideGainRuleHandler;
import com.wxxr.mobile.stock.app.service.handler.GetPushMessageSettingCommand;
import com.wxxr.mobile.stock.app.service.handler.GetPushMessageSettingHandler;
import com.wxxr.mobile.stock.app.service.handler.GetUserAuthSettingCommand;
import com.wxxr.mobile.stock.app.service.handler.GetUserAuthSettingHandler;
import com.wxxr.mobile.stock.app.service.handler.GetUserSignMessageHandler;
import com.wxxr.mobile.stock.app.service.handler.ReadAllUnreadMessageCommand;
import com.wxxr.mobile.stock.app.service.handler.ReadAllUnreadMessageHandler;
import com.wxxr.mobile.stock.app.service.handler.ReadPullMessageCommand;
import com.wxxr.mobile.stock.app.service.handler.ReadPullMessageHandler;
import com.wxxr.mobile.stock.app.service.handler.ReadRemindMessageCommand;
import com.wxxr.mobile.stock.app.service.handler.ReadRemindMessageHandler;
import com.wxxr.mobile.stock.app.service.handler.RefresUserInfoHandler;
import com.wxxr.mobile.stock.app.service.handler.RefreshUserInfoCommand;
import com.wxxr.mobile.stock.app.service.handler.RegisterHandher;
import com.wxxr.mobile.stock.app.service.handler.RestPasswordHandler;
import com.wxxr.mobile.stock.app.service.handler.SearchUserCommandHandler;
import com.wxxr.mobile.stock.app.service.handler.SignUpHandler;
import com.wxxr.mobile.stock.app.service.handler.SubmitAuthCommand;
import com.wxxr.mobile.stock.app.service.handler.SubmitPushMesasgeCommand;
import com.wxxr.mobile.stock.app.service.handler.SubmitPushMesasgeHandler;
import com.wxxr.mobile.stock.app.service.handler.SumitAuthHandler;
import com.wxxr.mobile.stock.app.service.handler.UpPwdCommand;
import com.wxxr.mobile.stock.app.service.handler.UpPwdHandler;
import com.wxxr.mobile.stock.app.service.handler.UpdateAuthCommand;
import com.wxxr.mobile.stock.app.service.handler.UpdateAuthHandler;
import com.wxxr.mobile.stock.app.service.handler.UpdateNickNameCommand;
import com.wxxr.mobile.stock.app.service.handler.UpdateNickNameHandler;
import com.wxxr.mobile.stock.app.service.handler.UpdateTokenCommand;
import com.wxxr.mobile.stock.app.service.handler.UpdateTokenHandler;
import com.wxxr.mobile.stock.app.service.loader.GainBeanLoader;
import com.wxxr.mobile.stock.app.service.loader.GainPayDetailLoader;
import com.wxxr.mobile.stock.app.service.loader.PersonalHomePageLoader;
import com.wxxr.mobile.stock.app.service.loader.PullMessageLoader;
import com.wxxr.mobile.stock.app.service.loader.RemindMessageLoader;
import com.wxxr.mobile.stock.app.service.loader.UNReadRemindingMessageLoader;
import com.wxxr.mobile.stock.app.service.loader.UserAssetLoader;
import com.wxxr.mobile.stock.app.service.loader.UserAttributeLoader;
import com.wxxr.mobile.stock.app.service.loader.UserClientInfoLoader;
import com.wxxr.mobile.stock.app.service.loader.UserInfoLoader;
import com.wxxr.mobile.stock.app.service.loader.VoucherLoader;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.security.vo.UserParamVO;
import com.wxxr.stock.common.valobject.ResultBaseVO;
import com.wxxr.stock.crm.customizing.ejb.api.ActivityUserVo;
import com.wxxr.stock.crm.customizing.ejb.api.SearchNickNameVO;
import com.wxxr.stock.crm.customizing.ejb.api.UserAttributeVO;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;
import com.wxxr.stock.notification.ejb.api.MessageVO;
import com.wxxr.stock.restful.json.ClientInfoVO;
import com.wxxr.stock.restful.json.SimpleVO;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVO;
import com.wxxr.stock.trading.ejb.api.GuideResultVO;
import com.wxxr.stock.trading.ejb.api.PullMessageVO;
import com.wxxr.stock.trading.ejb.api.StockResultVO;
import com.wxxr.stock.trading.ejb.api.UserAssetVO;
import com.wxxr.stock.trading.ejb.api.UserSignVO;

/**
 * @author neillin
 * 
 */
public class UserManagementServiceImpl extends AbstractModule<IStockAppContext>
		implements IUserManagementService, IStatefulService {

	private static final Trace log = Trace
			.register("com.wxxr.mobile.stock.app.service.impl.UserManagementServiceImpl");

	private ICommandExecutor cmdExecutor;

	/**
	 * 个人主页
	 */

	private UserAssetBean userAssetBean;

	private IReloadableEntityCache<String, UserAssetBean> userAssetBeanCache;

	private ClientInfoBean clientInfoBean;
	private IReloadableEntityCache<String, ClientInfoBean> clientInfoBean_cache;
	private VoucherBean voucherBean;
	private IReloadableEntityCache<String, VoucherBean> voucherBeanCache;

	private BindableListWrapper<RemindMessageBean> remindMessages;
	private GenericReloadableEntityCache<String, RemindMessageBean, MessageVO> remindMessagesCache = new GenericReloadableEntityCache<String, RemindMessageBean, MessageVO>(
			"remindMessageBean", 300) {

				/* (non-Javadoc)
				 * @see com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache#getReloadCommand(java.util.Map)
				 */
				@Override
				protected ICommand<List<MessageVO>> getReloadCommand(
						Map<String, Object> params) {
					IUserIdentityManager mgr = context.getService(IUserIdentityManager.class);
					if((mgr == null)||(mgr.isUserAuthenticated() == false)){
						return null;
					}
					return super.getReloadCommand(params);
				}
		
	};

	private BindableListWrapper<PullMessageBean> pullMessages;
	private GenericReloadableEntityCache<String, PullMessageBean, PullMessageVO> pullMessagesCache;

	private IReloadableEntityCache<String, AuthInfo> userAuthInfoCache;

	private BindableListWrapper<GainPayDetailBean> gainPayDetails;
	private GenericReloadableEntityCache<Long, GainPayDetailBean, GainPayDetailsVO> gainPayDetail_cache;

	private GenericReloadableEntityCache<String, PersonalHomePageBean, List<PersonalHomePageBean>> personalHomePageBean_cache;

	private GenericReloadableEntityCache<String, GainBean, List<GainBean>> gainBean_cache;

	private BindableListWrapper<RemindMessageBean> unreadRemindMessages;
	private GenericReloadableEntityCache<String, RemindMessageBean, RemindMessageBean> unreadRemindMessagesCache;

	private UserBean myUserInfo;

	private UserSignBean userSignBean;
	
	
	/**
	 * 用户昵称查询结果
	 */
	private SearchUserListBean searchUserListBean = new SearchUserListBean();

	// ============== module life cycle =================

	public void startService() {

		IEntityLoaderRegistry registry = getService(IEntityLoaderRegistry.class);
		registry.registerEntityLoader("userAssetBean", new UserAssetLoader());
		registry.registerEntityLoader("clientInfo", new UserClientInfoLoader());
		registry.registerEntityLoader("voucherBean", new VoucherLoader());
		registry.registerEntityLoader("remindMessageBean",
				new RemindMessageLoader());
		registry.registerEntityLoader("pullMessageBean",
				new PullMessageLoader());
		registry.registerEntityLoader("userAuthorInfo",
				new UserAttributeLoader());
		registry.registerEntityLoader("gainPayDetailBean",
				new GainPayDetailLoader());
		registry.registerEntityLoader("unreadRemindingMsg",
				new UNReadRemindingMessageLoader());

		registry.registerEntityLoader("myUserInfo", new UserInfoLoader());
		
	    //registry.registerEntityLoader("searchUserListBean", new SearchUserLoader());

		getCommandExecutor().registerCommandHandler(UpPwdHandler.COMMAND_NAME,
				new UpPwdHandler());
		getCommandExecutor().registerCommandHandler(
				UpdateAuthHandler.COMMAND_NAME, new UpdateAuthHandler());
		getCommandExecutor().registerCommandHandler(
				SumitAuthHandler.COMMAND_NAME, new SumitAuthHandler());
		getCommandExecutor().registerCommandHandler(
				SubmitPushMesasgeHandler.COMMAND_NAME,
				new SubmitPushMesasgeHandler());
		getCommandExecutor().registerCommandHandler(
				GetPushMessageSettingCommand.COMMAND_NAME,
				new GetPushMessageSettingHandler());
		getCommandExecutor()
				.registerCommandHandler(UpdateNickNameHandler.COMMAND_NAME,
						new UpdateNickNameHandler());
		getCommandExecutor()
				.registerCommandHandler(RefresUserInfoHandler.COMMAND_NAME,
						new RefresUserInfoHandler());
		getCommandExecutor().registerCommandHandler(
				RestPasswordHandler.COMMAND_NAME, new RestPasswordHandler());
		getCommandExecutor().registerCommandHandler(
				UpdateTokenHandler.COMMAND_NAME, new UpdateTokenHandler());
		getCommandExecutor().registerCommandHandler(
				RegisterHandher.COMMAND_NAME, new RegisterHandher());
		getCommandExecutor().registerCommandHandler(
				ReadRemindMessageHandler.COMMAND_NAME,
				new ReadRemindMessageHandler());
		getCommandExecutor().registerCommandHandler(
				ReadAllUnreadMessageHandler.COMMAND_NAME,
				new ReadAllUnreadMessageHandler());
		getCommandExecutor().registerCommandHandler(
				ReadPullMessageHandler.COMMAND_NAME,
				new ReadPullMessageHandler());
		
		getCommandExecutor().registerCommandHandler(
				SearchUserCommandHandler.COMMAND_NAME,
				new SearchUserCommandHandler());
		
		context.getService(ICommandExecutor.class).registerCommandHandler(
				SignUpCommand.COMMAND_NAME, new SignUpHandler());
		context.getService(ICommandExecutor.class).registerCommandHandler(
				GetUserSignMessageCommand.COMMAND_NAME,
				new GetUserSignMessageHandler());
		context.getService(ICommandExecutor.class).registerCommandHandler(
				GetGuideGainCommand.COMMAND_NAME, new GetGuideGainHandler());
		context.getService(ICommandExecutor.class).registerCommandHandler(
				GetGuideGainRuleCommand.COMMAND_NAME,
				new GetGuideGainRuleHandler());
		context.getService(ICommandExecutor.class)
				.registerCommandHandler(CheckGuideGainCommand.COMMAND_NAME,
						new CheckGuideGainHandler());
		context.getService(ICommandExecutor.class).registerCommandHandler(
				GetUserAuthSettingCommand.COMMAND_NAME,
				new GetUserAuthSettingHandler());
//		gainBean_cache = new GenericReloadableEntityCache<String, GainBean, List<GainBean>>(
//				"gainBean");
		registry.registerEntityLoader("personalHomePageBean",
				new PersonalHomePageLoader());
		registry.registerEntityLoader("gainBean", new GainBeanLoader());
		// getService(IEventRouter.class).registerEventListener(UserLoginEvent.class,
		// listener);
		context.registerService(IUserManagementService.class, this);

	}

	private void initData() {
		getCommandExecutor().submitCommand(new UpdateTokenCommand(), null);
		getUserSetting();
		// getUserAuthInfo();

	}

	public void stopService() {
		context.unregisterService(IUserManagementService.class, this);
		// getService(IEventRouter.class).unregisterEventListener(UserLoginEvent.class,
		// listener);
		userAssetBeanCache = null;
		voucherBeanCache = null;
		remindMessagesCache = null;
		pullMessagesCache = null;
		userAuthInfoCache = null;
		gainPayDetail_cache = null;
		personalHomePageBean_cache = null;
		gainBean_cache = null;
		unreadRemindMessagesCache = null;
	}

	protected ICommandExecutor getCommandExecutor() {
		if (this.cmdExecutor == null) {
			this.cmdExecutor = context.getService(ICommandExecutor.class);// getCommandExecutor();
		}
		return this.cmdExecutor;
	}

	@Override
	public UserBean getMyUserInfo() {
		myUserInfo = getService(IUserLoginManagementService.class)
				.getMyUserInfo();
		return myUserInfo;
	}

	public String getModuleName() {
		return "UserManagementService";
	}

	@Override
	public void pushMessageSetting(final boolean on)
			throws StockAppBizException {
		SubmitPushMesasgeCommand command = new SubmitPushMesasgeCommand();
		command.setBinding(on);
		AsyncUtils.execCommandAsyncInUI(getCommandExecutor(), command,
				new IDataConverter<Boolean, Void>() {
					public Void convert(Boolean value) {
						if (value != null && value.booleanValue()) {
							if (getMyUserInfo() != null) {
								myUserInfo.setMessagePushSettingOn(on);
							}
						}
						return null;
					}
				});

	}

	private void getUserSetting() {
		getCommandExecutor().submitCommand(new GetPushMessageSettingCommand(),
				new IAsyncCallback<SimpleResultVo>() {
					@Override
					public void success(SimpleResultVo result) {
						if (getMyUserInfo() != null) {
							myUserInfo.setMessagePushSettingOn(result != null
									&& result.getResult() == 1);
						}
					}

					@Override
					public void failed(Throwable cause) {

					}

					@Override
					public void cancelled() {

					}

					@Override
					public void setCancellable(ICancellable cancellable) {

					}
				});
		getCommandExecutor().submitCommand(new GetUserAuthSettingCommand(),
				new IAsyncCallback<Boolean>() {

					@Override
					public void success(Boolean result) {
						if (getMyUserInfo() != null) {
							myUserInfo.setBindCard(result);
						}
					}

					@Override
					public void failed(Throwable cause) {
						log.warn("Failed to get user auth setting", cause);
					}

					@Override
					public void cancelled() {

					}

					@Override
					public void setCancellable(ICancellable cancellable) {

					}
				});
		getCommandExecutor().submitCommand(new GetGuideGainRuleCommand(),
				new IAsyncCallback<SimpleVO>() {
					public void success(SimpleVO result) {
						if (result != null) {
							if (guideGainRule == null) {
								guideGainRule = new GuideGainBean();
							}
							guideGainRule.setGuideGain(result.getData());
						}
					}

					@Override
					public void failed(Throwable cause) {

					}

					@Override
					public void cancelled() {

					}

					@Override
					public void setCancellable(ICancellable cancellable) {

					}

				});
	}

	// @Override
	// public boolean getPushMessageSetting() {
	// GetPushMessageSettingCommand cmd=new GetPushMessageSettingCommand();
	// return AsyncUtils.execCommandAsyncInUI(getCommandExecutor(), cmd,
	// new IDataConverter<SimpleResultVo, Boolean>() {
	//
	// @Override
	// public Boolean convert(SimpleResultVo value) {
	// return value != null && value.getResult()==1;
	// }
	// }
	// );
	// }
	// private boolean getGuideGainAllow() {
	// try {
	// GuideResultVO vo =
	// RestUtils.getRestService(ITradingProtectedResource.class).checkGuideGainAllow();
	// if (vo!=null) {
	// if (getService(IUserIdentityManager.class).isUserAuthenticated()) {
	// getMyUserInfo().setAllowGuideGain(vo.getSuccOrNot()==1);
	// }
	// }
	// return vo.getSuccOrNot()==1;
	// } catch (Exception e) {
	// new StockAppBizException("系统错误");
	// }
	// return false;
	// }

	@Override
	public void updatePassword(String oldPwd, String newPwd, String newPwd2)
			throws StockAppBizException {

		UpPwdCommand cmd = new UpPwdCommand();
		cmd.setOldPwd(oldPwd);
		cmd.setNewPwd(newPwd);
		cmd.setNewPwd2(newPwd2);
		AsyncUtils.execCommandAsyncInUI(getCommandExecutor(), cmd);
		// try {
		// Future<ResultBaseVO> future=getCommandExecutor().submitCommand(cmd);
		//
		// ResultBaseVO vo=future.get(30,TimeUnit.SECONDS);
		// if(vo.getResulttype()!=1){
		// throw new StockAppBizException(vo.getResultInfo());
		// }
		// }catch(StockAppBizException e){
		// throw e;
		// }catch(CommandException e){
		// throw new StockAppBizException(e.getMessage());
		// }catch (Exception e) {
		// throw new StockAppBizException("系统错误");
		// }
	}

	public void switchBankCard(String accountName, String bankName,
			String bankAddr, String bankNum) {
		UpdateAuthCommand cmd = new UpdateAuthCommand();
		cmd.setAccountName(accountName);
		cmd.setBankName(bankName);
		cmd.setBankNum(bankNum);
		cmd.setBankAddr(bankAddr);
		AsyncUtils.execCommandAsyncInUI(getCommandExecutor(), cmd);
		// try{
		// Future<ResultBaseVO> future=getCommandExecutor().submitCommand(cmd);
		// try {
		// ResultBaseVO vo=future.get(30,TimeUnit.SECONDS);
		// if(vo.getResulttype()!=1){
		// throw new StockAppBizException(vo.getResultInfo());
		// }
		// } catch (Exception e) {
		// throw new StockAppBizException("系统错误");
		// }
		// }catch(CommandException e){
		// throw new StockAppBizException(e.getMessage());
		// }
	}

	public void withDrawCashAuth(String accountName, String bankName,
			String bankAddr, String bankNum) {
		SubmitAuthCommand cmd = new SubmitAuthCommand();
		cmd.setAccountName(accountName);
		cmd.setBankName(bankName);
		cmd.setBankNum(bankNum);
		cmd.setBankAddr(bankAddr);
		AsyncUtils.execCommandAsyncInUI(cmd,
				new IDataConverter<ResultBaseVO, Object>() {

					@Override
					public Object convert(ResultBaseVO vo)
							throws NestedRuntimeException {
						if (vo != null) {
							if (vo.getResulttype() == 1) {
								if (myUserInfo != null) {
									myUserInfo.setBindCard(vo != null
											&& vo.getResulttype() == 1);
								}
							} else {
								throw new NestedRuntimeException(
										new StockAppBizException(vo
												.getResultInfo()));
							}
						}
						return null;
					}
				});

	}

	public AuthInfo getUserAuthInfo() {
		if (userAuthInfoCache == null) {
			userAuthInfoCache = new GenericReloadableEntityCache<String, AuthInfo, UserAttributeVO>(
					"userAuthorInfo");
		}
		final String key = getService(IUserIdentityManager.class).getUserId();
		AuthInfo bean = this.userAuthInfoCache.getEntity(key);
		if (bean == null) {
			bean = new AuthInfo();
			userAuthInfoCache.putEntity(key, bean);
		}
		AsyncUtils.forceLoadNFetchAsyncInUI(userAuthInfoCache, null,
				new AsyncFuture<AuthInfo>(), new IEntityFetcher<AuthInfo>() {
					@Override
					public AuthInfo fetchFromCache(
							IBindableEntityCache<?, ?> cache) {
						if (getService(IUserIdentityManager.class)
								.isUserAuthenticated()) {
							AuthInfo _bean = (AuthInfo) cache.getEntity(key);
							if (_bean != null
									&& !StringUtils.isBlank(_bean
											.getAccountName())) {
								getMyUserInfo().setBindCard(true);
							}
						}
						return (AuthInfo) cache.getEntity(key);
					}
				});
		return bean;
	}

	@Override
	public PersonalHomePageBean getMyPersonalHomePage(boolean wait4Finish) {
		if (personalHomePageBean_cache == null) {
			personalHomePageBean_cache = new GenericReloadableEntityCache<String, PersonalHomePageBean, List<PersonalHomePageBean>>(
					"personalHomePageBean");
		}
		final String key = getService(IUserIdentityManager.class).getUserId();
		if (wait4Finish) {
			return AsyncUtils.forceLoadNFetchAsyncInUI(
					personalHomePageBean_cache, null,
					new AsyncFuture<PersonalHomePageBean>(),
					new IEntityFetcher<PersonalHomePageBean>() {

						@Override
						public PersonalHomePageBean fetchFromCache(
								IBindableEntityCache<?, ?> cache) {
							return personalHomePageBean_cache.getEntity(key);
						}
					});
		} else {
			personalHomePageBean_cache.doReload(false, null, null);
		}
		/*
		 * if (personalHomePageBean_cache.getEntity(key)==null){
		 * PersonalHomePageBean b=new PersonalHomePageBean();
		 * personalHomePageBean_cache.putEntity(key,b); }
		 */
		return personalHomePageBean_cache.getEntity(key);
	}

	public BindableListWrapper<GainBean> getMorePersonalRecords(int start,
			int limit, final boolean virtual) {
		if (gainBean_cache == null) {
			gainBean_cache = new GenericReloadableEntityCache<String, GainBean, List<GainBean>>(
					"gainBean") {
				@Override
				protected Map<String, Object> prepareLoadmoreCommandParameter(
						BindableListWrapper<GainBean> list) {
					Map<String, Object> params = new HashMap<String, Object>();
					int start = gainBean_cache.getCacheSize();
					params.put("virtual", virtual);
					params.put("start", start);
					params.put("limit", 20);
					return params;
				}
			};
		}
		final BindableListWrapper<GainBean> gainBeans = gainBean_cache
				.getEntities(new IEntityFilter<GainBean>() {
					@Override
					public boolean doFilter(GainBean entity) {
						if (StringUtils.isNotBlank(entity.getUserId())
								&& entity.getUserId().equals(
										getService(IUserIdentityManager.class)
												.getUserId())
								&& entity.getVirtual() == virtual) {
							return true;
						}
						return false;
					}

				}, viewMoreComparator);
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("virtual", virtual);
		p.put("start", start);
		p.put("limit", limit);
		// gainBean_cache.doReload(true,p,null);
		gainBean_cache.setCommandParameters(p);
		gainBeans.setReloadParameters(p);
		return AsyncUtils.forceLoadNFetchAsyncInUI(gainBean_cache, p,
				new AsyncFuture<BindableListWrapper<GainBean>>(),
				new IEntityFetcher<BindableListWrapper<GainBean>>() {

					@Override
					public BindableListWrapper<GainBean> fetchFromCache(
							IBindableEntityCache<?, ?> cache) {
						return gainBeans;
					}
				});
	}

	private static Comparator<GainBean> viewMoreComparator = new Comparator<GainBean>() {

		@Override
		public int compare(GainBean o1, GainBean o2) {
			if (o2 != null && o1 != null) {
				return (int) (o2.getTradingAccountId() - o1
						.getTradingAccountId());
			}
			return 0;
		}
	};

	@Override
	public UserAssetBean getUserAssetBean() {
		if (userAssetBeanCache == null) {
			userAssetBeanCache = new GenericReloadableEntityCache<String, UserAssetBean, UserAssetVO>(
					"userAssetBean");
		}
		userAssetBean = userAssetBeanCache.getEntity(KUtils.getService(
				IUserIdentityManager.class).getUserId());
		if (userAssetBean == null) {
			userAssetBean = new UserAssetBean();
			userAssetBeanCache.putEntity(
					KUtils.getService(IUserIdentityManager.class).getUserId(),
					userAssetBean);
		}
		userAssetBeanCache.doReload(false, null, null);
		return userAssetBean;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.mobile.stock.app.service.IUserManagementService#getVoucherBean()
	 */
	@Override
	public VoucherBean getVoucherBean() {
		if (voucherBean == null) {
			if (voucherBeanCache == null) {
				voucherBeanCache = new GenericReloadableEntityCache<String, VoucherBean, ActivityUserVo>(
						"voucherBean");
			}
			voucherBean = voucherBeanCache.getEntity(VoucherBean.class
					.getCanonicalName());
			if (voucherBean == null) {
				voucherBean = new VoucherBean();
				voucherBeanCache.putEntity(
						VoucherBean.class.getCanonicalName(), voucherBean);
			}
		}
		voucherBeanCache.doReload(false, null, null);
		return voucherBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.mobile.stock.app.service.IUserManagementService#getRemindMessageBean
	 * ()
	 */
	@Override
	public BindableListWrapper<RemindMessageBean> getRemindMessageBean() {
		if (remindMessages == null) {
			if (remindMessagesCache == null) {
				remindMessagesCache = new GenericReloadableEntityCache<String, RemindMessageBean, MessageVO>(
						"remindMessageBean", 300) {
					protected Map<String, Object> prepareLoadmoreCommandParameter(
							BindableListWrapper<RemindMessageBean> list) {
						Map<String, Object> params = new HashMap<String, Object>();
						List<RemindMessageBean> data = list.getData();
						int start = data != null ? data.size() : 0;
						params.put("start", start);
						params.put("limit", 20);
						return params;
					}
				};
			}
			remindMessages = remindMessagesCache.getEntities(null,
					new Comparator<RemindMessageBean>() {

						@Override
						public int compare(RemindMessageBean lhs,
								RemindMessageBean rhs) {
							long c = rhs.getCreatedDate().compareTo(
									lhs.getCreatedDate());
							if (c == 0) {
								c = ((String) rhs.getAttrs().get("time"))
										.compareTo((String) lhs.getAttrs().get(
												"time"));
							}
							return c >= 0 ? 1 : -1;
						}
					});
		}
		remindMessagesCache.doReload(false, null, null);
		return remindMessages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.mobile.stock.app.service.IUserManagementService#getPullMessageBean
	 * ()
	 */
	@Override
	public BindableListWrapper<PullMessageBean> getPullMessageBean(int start,
			int limit) {
		return getPullMessageBean(start, limit, true);
	}

	public BindableListWrapper<PullMessageBean> getPullMessageBean(int start,
			int limit, boolean wait4Finish) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("limit", limit);
		if (pullMessages == null) {
			if (pullMessagesCache == null) {
				pullMessagesCache = new GenericReloadableEntityCache<String, PullMessageBean, PullMessageVO>(
						"pullMessageBean") {

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache
					 * #prepareLoadmoreCommandParameter(java.util.Map)
					 */
					@Override
					protected Map<String, Object> prepareLoadmoreCommandParameter(
							BindableListWrapper<PullMessageBean> list) {
						Map<String, Object> params = new HashMap<String, Object>();
						List<PullMessageBean> data = list.getData();
						int start = data != null ? data.size() : 0;
						params.put("start", start);
						params.put("limit", 20);
						return params;
					}

				};
			}
			pullMessages = pullMessagesCache.getEntities(null,
					new Comparator<PullMessageBean>() {

						@Override
						public int compare(PullMessageBean lhs,
								PullMessageBean rhs) {
							long c = rhs.getCreateDate().compareTo(
									lhs.getCreateDate());
							return c >= 0 ? 1 : -1;
						}

					});
		}

		if (wait4Finish) {
			return AsyncUtils.forceLoadNFetchAsyncInUI(pullMessagesCache,
					params,
					new AsyncFuture<BindableListWrapper<PullMessageBean>>(),
					new IEntityFetcher<BindableListWrapper<PullMessageBean>>() {

						@Override
						public BindableListWrapper<PullMessageBean> fetchFromCache(
								IBindableEntityCache<?, ?> cache) {
							return pullMessages;
						}
					});
		} else {
			pullMessagesCache.doReload(false, params, null);
		}
		return pullMessages;
	}

	private static final Comparator<GainPayDetailBean> gainPayDetailComparator = new Comparator<GainPayDetailBean>() {

		@Override
		public int compare(GainPayDetailBean o1, GainPayDetailBean o2) {

			return o2.getTime() - o1.getTime() > 0 ? 1 : -1;
		}
	};

	@Override
	public BindableListWrapper<GainPayDetailBean> getGPDetails(int start,
			int limit) {
		return getGPDetails(start, limit, true);
	}
	//余额明细
	public BindableListWrapper<GainPayDetailBean> getGPDetails(int start,
			int limit, boolean wait4Finish) {
		if (gainPayDetail_cache == null) {
			gainPayDetail_cache = new GenericReloadableEntityCache<Long, GainPayDetailBean, GainPayDetailsVO>(
					"gainPayDetailBean") {
				@Override
				protected Map<String, Object> prepareLoadmoreCommandParameter(
						BindableListWrapper<GainPayDetailBean> list) {
					Map<String, Object> params = new HashMap<String, Object>();
					List<GainPayDetailBean> data = list.getData();
					int start = data != null ? data.size() : 0;
					params.put("start", start);
					params.put("limit", 20);
					return params;
				}
			};
		}
		if (gainPayDetails == null) {
			gainPayDetails = gainPayDetail_cache.getEntities(null,
					gainPayDetailComparator);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("limit", limit);
		gainPayDetail_cache.setCommandParameters(params);
		if (wait4Finish) {
			return AsyncUtils
					.forceLoadNFetchAsyncInUI(
							gainPayDetail_cache,
							params,
							new AsyncFuture<BindableListWrapper<GainPayDetailBean>>(),
							new IEntityFetcher<BindableListWrapper<GainPayDetailBean>>() {

								@Override
								public BindableListWrapper<GainPayDetailBean> fetchFromCache(
										IBindableEntityCache<?, ?> cache) {
									return gainPayDetails;
								}
							});
		} else {
			gainPayDetail_cache.doReload(false, params, null);
		}
		gainPayDetails.setReloadParameters(params);
		return gainPayDetails;
	}

	@Override
	public void updateNickName(String nickName) {
		UpdateNickNameCommand cmd = new UpdateNickNameCommand();
		cmd.setNickName(nickName);
		AsyncUtils.execCommandAsyncInUI(cmd,
				new IDataConverter<ResultBaseVO, Object>() {
					@Override
					public Object convert(ResultBaseVO vo)
							throws NestedRuntimeException {
						if (vo.getResulttype() != 1) {
							throw new NestedRuntimeException(
									new StockAppBizException(vo.getResultInfo()));
						}
						return null;
					}
				});
		// try{
		// Future<ResultBaseVO> future=getCommandExecutor().submitCommand(cmd);
		// ResultBaseVO vo=future.get(30,TimeUnit.SECONDS);
		// if(vo.getResulttype()!=1){
		// throw new StockAppBizException(vo.getResultInfo());
		// }
		// }catch(StockAppBizException e){
		// throw e;
		// }catch(CommandException e){
		// throw new StockAppBizException(e.getMessage());
		// } catch (Exception e) {
		// throw new StockAppBizException("网络不给力");
		// }
	}

	@Override
	public UserBean refreshUserInfo() {
		if (myUserInfo == null) {
			myUserInfo = new UserBean();
		}
		RefreshUserInfoCommand command = new RefreshUserInfoCommand();
		return AsyncUtils.execCommandAsyncInUI(command,
				new IDataConverter<UserVO, UserBean>() {

					@Override
					public UserBean convert(UserVO userVO)
							throws NestedRuntimeException {
						if (userVO != null) {
							myUserInfo.setNickName(userVO.getNickName());
							myUserInfo.setUsername(userVO.getUserName());
							myUserInfo.setPhoneNumber(userVO.getMoblie());
						}
						return myUserInfo;
					}
				});
		// Future<UserVO> future=getCommandExecutor().submitCommand(command);
		// try {
		// UserVO userVO=future.get(30,TimeUnit.SECONDS);
		// myUserInfo.setNickName(userVO.getNickName());
		// myUserInfo.setUsername(userVO.getUserName());
		// myUserInfo.setPhoneNumber(userVO.getMoblie());
		// } catch (Exception e) {
		// new StockAppBizException("系统错误");
		// }
		// return myUserInfo;
	}

	protected void updateToken() {
		UpdateTokenCommand command = new UpdateTokenCommand();
		AsyncUtils.execCommandAsyncInUI(command);
		// try{
		// Future<TokenVO> future=getCommandExecutor().submitCommand(command);
		// }catch(Throwable e){
		// log.warn("updatToken error",e);
		// }
	}

	@Override
	public void readRemindMessage(String read) {
		ReadRemindMessageCommand command = new ReadRemindMessageCommand();
		command.setId(read);
		AsyncUtils.execCommandAsyncInUI(getCommandExecutor(), command);
	}

	@Override
	public BindableListWrapper<RemindMessageBean> getUnreadRemindMessages() {
		if (unreadRemindMessages == null) {
			if (unreadRemindMessagesCache == null) {
				unreadRemindMessagesCache = new GenericReloadableEntityCache<String, RemindMessageBean, RemindMessageBean>(
						"unreadRemindingMsg", 60);
			}
			unreadRemindMessages = unreadRemindMessagesCache.getEntities(null,
					new Comparator<RemindMessageBean>() {

						@Override
						public int compare(RemindMessageBean lhs,
								RemindMessageBean rhs) {
							long c = rhs.getCreatedDate().compareTo(
									lhs.getCreatedDate());
							if (c == 0) {
								c = ((String) rhs.getAttrs().get("time"))
										.compareTo((String) lhs.getAttrs().get(
												"time"));
							}
							return c >= 0 ? 1 : -1;
						}
					});
		}
		unreadRemindMessagesCache.doReload(false, null, null);
		return unreadRemindMessages;
	}

	@Override
	public void readAllUnremindMessage() {
		ReadAllUnreadMessageCommand command = new ReadAllUnreadMessageCommand();
		AsyncUtils.execCommandAsyncInUI(getCommandExecutor(), command);
	}

	@Override
	public ClientInfoBean getClientInfo() {
		if (clientInfoBean_cache == null) {
			clientInfoBean_cache = new GenericReloadableEntityCache<String, ClientInfoBean, ClientInfoVO>(
					"clientInfo");
		}
		clientInfoBean = clientInfoBean_cache.getEntity(ClientInfoBean.class
				.getCanonicalName());
		if (clientInfoBean == null) {
			clientInfoBean = new ClientInfoBean();
			clientInfoBean_cache.putEntity(
					ClientInfoBean.class.getCanonicalName(), clientInfoBean);
		}
		clientInfoBean_cache.doReload(true, null, null);
		return clientInfoBean;
	}

	@Override
	public void readPullMesage(long id) {
		ReadPullMessageCommand command = new ReadPullMessageCommand();
		command.setId(id);
		AsyncUtils.execCommandAsyncInUI(getCommandExecutor(), command);
	}

	protected <S> S getService(Class<S> clazz) {
		return this.context.getService(clazz);
	}

	@Override
	public void destroy(Object serviceHandler) {
		if (userAssetBeanCache != null) {
			userAssetBeanCache.clear();
		}
		if (voucherBeanCache != null) {
			voucherBeanCache.clear();
		}
		if (remindMessagesCache != null) {
			remindMessagesCache.clear();
		}
		if (pullMessagesCache!=null) {
			pullMessagesCache.clear();
		}
		userAuthInfoCache = null;
		if (gainPayDetail_cache!=null) {
			gainPayDetail_cache.clear();
		}
		if (personalHomePageBean_cache != null) {
			personalHomePageBean_cache.clear();
		}
		if (gainBean_cache!=null) {
			gainBean_cache.clear();
		}
		if (unreadRemindMessagesCache!=null) {
			unreadRemindMessagesCache.clear();
		}
	}

	public IServiceDecoratorBuilder getDecoratorBuilder() {
		return new IServiceDecoratorBuilder() {

			@Override
			public <T> T createServiceDecorator(Class<T> clazz,
					final IServiceDelegateHolder<T> holder) {
				if (clazz == IUserManagementService.class) {
					return clazz.cast(new IUserManagementService() {

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getMyUserInfo()
						 */
						public UserBean getMyUserInfo() {
							return ((IUserManagementService) holder
									.getDelegate()).getMyUserInfo();
						}

						/**
						 * @param oldPwd
						 * @param newPwd
						 * @param newPwd2
						 * @throws StockAppBizException
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#updatePassword(java.lang.String,
						 *      java.lang.String, java.lang.String)
						 */
						public void updatePassword(String oldPwd,
								String newPwd, String newPwd2)
								throws StockAppBizException {
							((IUserManagementService) holder.getDelegate())
									.updatePassword(oldPwd, newPwd, newPwd2);
						}

						/**
						 * @param on
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#pushMessageSetting(boolean)
						 */
						public void pushMessageSetting(boolean on) {
							((IUserManagementService) holder.getDelegate())
									.pushMessageSetting(on);
						}

						/**
						 * @param bankName
						 * @param bankAddr
						 * @param bankNum
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#switchBankCard(java.lang.String,
						 *      java.lang.String, java.lang.String)
						 */
						public void switchBankCard(String accountName,
								String bankName, String bankAddr, String bankNum) {
							((IUserManagementService) holder.getDelegate())
									.switchBankCard(accountName, bankName,
											bankAddr, bankNum);
						}

						/**
						 * @param accountName
						 * @param bankName
						 * @param bankAddr
						 * @param bankNum
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#withDrawCashAuth(java.lang.String,
						 *      java.lang.String, java.lang.String,
						 *      java.lang.String)
						 */
						public void withDrawCashAuth(String accountName,
								String bankName, String bankAddr, String bankNum) {
							((IUserManagementService) holder.getDelegate())
									.withDrawCashAuth(accountName, bankName,
											bankAddr, bankNum);
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getUserAuthInfo()
						 */
						public AuthInfo getUserAuthInfo() {
							return ((IUserManagementService) holder
									.getDelegate()).getUserAuthInfo();
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getMyPersonalHomePage()
						 */
						public PersonalHomePageBean getMyPersonalHomePage(
								boolean wait4finish) {
							return ((IUserManagementService) holder
									.getDelegate())
									.getMyPersonalHomePage(wait4finish);
						}

						/**
						 * @param start
						 * @param limit
						 * @param virtual
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getMorePersonalRecords(int,
						 *      int, boolean)
						 */
						public BindableListWrapper<GainBean> getMorePersonalRecords(
								int start, int limit, boolean virtual) {
							return ((IUserManagementService) holder
									.getDelegate()).getMorePersonalRecords(
									start, limit, virtual);
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getUserAssetBean()
						 */
						public UserAssetBean getUserAssetBean() {
							return ((IUserManagementService) holder
									.getDelegate()).getUserAssetBean();
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getVoucherBean()
						 */
						public VoucherBean getVoucherBean() {
							return ((IUserManagementService) holder
									.getDelegate()).getVoucherBean();
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getRemindMessageBean()
						 */
						public BindableListWrapper<RemindMessageBean> getRemindMessageBean() {
							return ((IUserManagementService) holder
									.getDelegate()).getRemindMessageBean();
						}

						/**
						 * @param start
						 * @param limit
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getPullMessageBean(int,
						 *      int)
						 */
						public BindableListWrapper<PullMessageBean> getPullMessageBean(
								int start, int limit) {
							return ((IUserManagementService) holder
									.getDelegate()).getPullMessageBean(start,
									limit);
						}

						/**
						 * @param nickName
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#updateNickName(java.lang.String)
						 */
						public void updateNickName(String nickName) {
							((IUserManagementService) holder.getDelegate())
									.updateNickName(nickName);
						}

						/**
						 * @param start
						 * @param limit
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getGPDetails(int,
						 *      int)
						 */
						public BindableListWrapper<GainPayDetailBean> getGPDetails(
								int start, int limit) {
							return ((IUserManagementService) holder
									.getDelegate()).getGPDetails(start, limit);
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#refreshUserInfo()
						 */
						public UserBean refreshUserInfo() {
							return ((IUserManagementService) holder
									.getDelegate()).refreshUserInfo();
						}

						/**
						 * @param id
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#readRemindMessage(java.lang.String)
						 */
						public void readRemindMessage(String id) {
							((IUserManagementService) holder.getDelegate())
									.readRemindMessage(id);
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getUnreadRemindMessages()
						 */
						public BindableListWrapper<RemindMessageBean> getUnreadRemindMessages() {
							return ((IUserManagementService) holder
									.getDelegate()).getUnreadRemindMessages();
						}

						/**
						 * 
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#readAllUnremindMessage()
						 */
						public void readAllUnremindMessage() {
							((IUserManagementService) holder.getDelegate())
									.readAllUnremindMessage();
						}

						/**
						 * @param id
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#readPullMesage(long)
						 */
						public void readPullMesage(long id) {
							((IUserManagementService) holder.getDelegate())
									.readPullMesage(id);
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.IUserManagementService#getClientInfo()
						 */
						public ClientInfoBean getClientInfo() {
							return ((IUserManagementService) holder
									.getDelegate()).getClientInfo();
						}

						@Override
						public boolean verfiy(String userId, String password) {
							return ((IUserManagementService) holder
									.getDelegate()).verfiy(userId, password);
						}

						@Override
						public UserSignBean getUserSignBean() {
							return ((IUserManagementService) holder
									.getDelegate()).getUserSignBean();
						}

						@Override
						public UserSignBean sign() {
							return ((IUserManagementService) holder
									.getDelegate()).sign();
						}

						@Override
						public GuideGainBean getGuideGainRule() {
							return ((IUserManagementService) holder
									.getDelegate()).getGuideGainRule();
						}

						@Override
						public void getGuideGain() {
							((IUserManagementService) holder.getDelegate())
									.getGuideGain();

						}

						@Override
						public GuideGainBean checkGuideGain() {
							return ((IUserManagementService) holder
									.getDelegate()).checkGuideGain();

						}

						@Override
						public SearchUserListBean searchByNickName(String nickName) {
							return ((IUserManagementService) holder
									.getDelegate()).searchByNickName(nickName);
						}

					});
				}
				throw new IllegalArgumentException("Invalid service class :"
						+ clazz);
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		try {
			UserManagementServiceImpl impl = (UserManagementServiceImpl) super
					.clone();
			initData();
			return impl;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("SHOULD NOT HAPPEN !");
		}
	}

	@Override
	protected void initServiceDependency() {
		addRequiredService(ISessionManager.class);
		addRequiredService(IRestProxyService.class);
		addRequiredService(IEntityLoaderRegistry.class);
		addRequiredService(ICommandExecutor.class);
		addRequiredService(IPreferenceManager.class);
		addRequiredService(IUserIdentityManager.class);
		addRequiredService(IUserLoginManagementService.class);
	}

	@Override
	public boolean verfiy(String userId, String password) {
		if (getService(IUserIdentityManager.class).getUserId().equals(userId)) {
			return getMyUserInfo() != null
					&& StringUtils.isNotBlank(getMyUserInfo().getPassword())
					&& getMyUserInfo().getPassword().equals(password);
		}
		return false;
	}

	@Override
	public UserSignBean getUserSignBean() {
		if (getService(IUserIdentityManager.class).isUserAuthenticated()) {
			if (userSignBean == null) {
				userSignBean = new UserSignBean();
			}
			GetUserSignMessageCommand cmd = new GetUserSignMessageCommand();
			return AsyncUtils.execCommandAsyncInUI(cmd,
					new IDataConverter<UserSignVO, UserSignBean>() {
						@Override
						public UserSignBean convert(UserSignVO vo)
								throws NestedRuntimeException {
							if (vo != null) {
								userSignBean.setRewardVol(vo.getRewardVol());
								userSignBean.setOngoingDays(vo.getOngoingDays());
								userSignBean.setSign(vo.getSign());
								userSignBean.setSignDate(vo.getSignDate());
							}
							return userSignBean;
						}
					});
		} else {
			throw new StockAppBizException("用户未登录");
		}
	}

	@Override
	public UserSignBean sign() {
		if (getService(IUserIdentityManager.class).isUserAuthenticated()) {
			if (userSignBean == null) {
				userSignBean = new UserSignBean();
			}
			SignUpCommand cmd = new SignUpCommand();
			return AsyncUtils.execCommandAsyncInUI(cmd,
					new IDataConverter<UserSignVO, UserSignBean>() {
						@Override
						public UserSignBean convert(UserSignVO vo)
								throws NestedRuntimeException {
							if (vo != null) {
								if (vo.getSuccess() == -1) {
									throw new NestedRuntimeException(
											new StockAppBizException(vo
													.getFailReason()));
								} else if (vo.getSuccess() == 0) {
									throw new NestedRuntimeException(
											new StockAppBizException(
													"今日已签到，请勿重复操作"));
								} else {
									userSignBean.setRewardVol(vo.getRewardVol());
									userSignBean.setOngoingDays(vo
											.getOngoingDays());
									userSignBean.setSign(true);
									userSignBean.setSignDate(vo.getSignDate());
									KUtils.getService(
											ITradingManagementService.class)
											.getHomeMenuList(true);// 签到成功后按需刷新首页菜单
								}
							}
							return userSignBean;
						}
					});
		} else {
			throw new StockAppBizException("用户未登录");
		}
	}

	GuideGainBean guideGainRule;

	@Override
	public GuideGainBean getGuideGainRule() {
		if (guideGainRule == null) {
			guideGainRule = new GuideGainBean();
		}
		GetGuideGainRuleCommand cmd = new GetGuideGainRuleCommand();
		AsyncUtils.execCommandAsyncInUI(cmd,
				new IDataConverter<SimpleVO, Object>() {
					@Override
					public Object convert(SimpleVO vo)
							throws NestedRuntimeException {
						if (vo != null) {
							if (StringUtils.isNotBlank(vo.getData())) {
								guideGainRule.setGuideGain(vo.getData());
							}
						}
						return guideGainRule;
					}
				});
		return guideGainRule;
	}

	@Override
	public void getGuideGain() {
		if (getService(IUserIdentityManager.class).isUserAuthenticated()) {
			GetGuideGainCommand cmd = new GetGuideGainCommand();
			AsyncUtils.execCommandAsyncInUI(cmd,
					new IDataConverter<StockResultVO, Object>() {

						@Override
						public Object convert(StockResultVO vo)
								throws NestedRuntimeException {
							if (vo != null) {
								if (vo.getSuccOrNot() == 0) {
									throw new NestedRuntimeException(
											new StockAppBizException(vo
													.getCause()));
								} else {
									if (guideGain == null) {
										guideGain = new GuideGainBean();
									}
									guideGain.setAllow(false);
								}
							}
							return null;
						}
					});
		} else {
			throw new StockAppBizException("用户未登录");
		}

	}

	GuideGainBean guideGain;

	public GuideGainBean checkGuideGain() {
		if (guideGain == null) {
			guideGain = new GuideGainBean();
		}
		if (getService(IUserIdentityManager.class).isUserAuthenticated()) {
			CheckGuideGainCommand cmd = new CheckGuideGainCommand();
			AsyncUtils.execCommandAsyncInUI(cmd,
					new IDataConverter<GuideResultVO, Object>() {

						@Override
						public Object convert(GuideResultVO vo)
								throws NestedRuntimeException {
							if (vo != null) {
								if (vo.getSuccOrNot() == 1) {
									guideGain.setAllow(true);
									guideGain
											.setGuideGain(guideGainRule == null ? ""
													: guideGainRule
															.getGuideGain());
								} else {
									guideGain.setAllow(false);
									guideGain.setGuideGain(vo.getAmount() + "");
								}
							}
							return guideGain;
						}
					});
		} else {
			guideGain.setAllow(true);
			guideGain.setGuideGain(guideGainRule == null ? "" : guideGainRule
					.getGuideGain());
		}
		return guideGain;
	}
	
	@Override
	public SearchUserListBean searchByNickName(String nickName) {
		log.debug("=============================start searchByNickName("+nickName+")");
		SearchUserCommand command = new SearchUserCommand();
		UserParamVO vo = new UserParamVO();
		vo.setNickName(nickName);
		command.setUserParamVo(vo);
		return AsyncUtils.execCommandAsyncInUI(command, new IDataConverter<SearchNickNameVO, SearchUserListBean>() {
			@Override
			public SearchUserListBean convert(SearchNickNameVO vo) throws NestedRuntimeException {
	
				if (vo != null) {
					List<UserWrapper> list = new ArrayList<UserWrapper>();
					List<UserVO> userVOs = vo.getNickList();
					for (UserVO userVO : userVOs) {
						UserWrapper userWrapper = new UserWrapper(userVO);
						
						list.add(userWrapper);
					}
					searchUserListBean.setSearchResult(list);
				}
				
				log.debug("searchUserList="+(searchUserListBean.getSearchResult()==null?"null":searchUserListBean.getSearchResult().size()));
				return searchUserListBean;
			}
		});
//		log.debug("searchUserList="+(searchUserListBean.getSearchResult()==null?"null":searchUserListBean.getSearchResult().size()));
//		return searchUserListBean;

	}

}
