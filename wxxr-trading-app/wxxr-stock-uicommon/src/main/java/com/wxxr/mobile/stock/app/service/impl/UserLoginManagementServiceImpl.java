/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.wxxr.javax.ws.rs.NotAuthorizedException;
import com.wxxr.mobile.android.preference.DictionaryUtils;
import com.wxxr.mobile.core.api.IUserAuthCredential;
import com.wxxr.mobile.core.api.IUserAuthManager;
import com.wxxr.mobile.core.api.UsernamePasswordCredential;
import com.wxxr.mobile.core.async.api.Async;
import com.wxxr.mobile.core.async.api.AsyncFuture;
import com.wxxr.mobile.core.async.api.DelegateCallback;
import com.wxxr.mobile.core.async.api.ExecAsyncException;
import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.core.async.api.IDataConverter;
import com.wxxr.mobile.core.async.api.NestedRuntimeException;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.event.api.IEventRouter;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.rpc.http.api.HttpRpcService;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.security.api.LoginAction;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.preference.api.IPreferenceManager;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.LoginFailedException;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.command.UserRegister2Command;
import com.wxxr.mobile.stock.app.common.AsyncUtils;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.IBindableEntityCache;
import com.wxxr.mobile.stock.app.common.IEntityFetcher;
import com.wxxr.mobile.stock.app.common.IEntityFilter;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.event.UserLoginEvent;
import com.wxxr.mobile.stock.app.service.IUserLoginManagementService;
import com.wxxr.mobile.stock.app.service.handler.Register2Handher;
import com.wxxr.mobile.stock.app.service.handler.RestPasswordCommand;
import com.wxxr.mobile.stock.app.service.handler.UserRegisterCommand;
import com.wxxr.mobile.stock.app.service.loader.GainBeanLoader;
import com.wxxr.mobile.stock.app.service.loader.OtherPersonalHomePageLoader;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.restful.resource.StockUserResourceAsync;

/**
 * @author wangyan
 *
 */
public class UserLoginManagementServiceImpl extends AbstractModule<IStockAppContext> implements IUserLoginManagementService,IUserAuthManager{

	private static final Trace log = Trace.register("com.wxxr.mobile.stock.app.service.impl.UserLoginManagementServiceImpl");
	private static final String KEY_USERNAME = "U";
	private static final String KEY_PASSWORD = "P";
	private static final String KEY_UPDATE_DATE = "UD";
	

    private static final String KEY_NICKNAME = "NickName";
    private static final String KEY_PHONENUMBER = "PhoneNumber";
    private static final String KEY_USERPIC = "UserPic";//用户图像
    private static final String KEY_MSG_SETTTING = "msgSetting";//消息推送设置
    private static final String KEY_HOME_BGIMG = "homeImg";//背景图片
    private static final String KEY_BINDCARD = "bindCard";//是否绑定银行卡
    
	private IPreferenceManager prefManager;
	private UsernamePasswordCredential usernamePasswordCredential4Login;
	// ==================beans =============================
	private UserBean myUserInfo ;
	private GenericReloadableEntityCache<String,GainBean,List<GainBean>> otherGainBean_cache;
    private GenericReloadableEntityCache<String,PersonalHomePageBean,List<PersonalHomePageBean>> otherpersonalHomePageBean_cache;
	@Override
	public  void login(final String userId, final String pwd) throws LoginFailedException {

		usernamePasswordCredential4Login = new UsernamePasswordCredential(
				userId, pwd);
		AsyncFuture<UserVO> future = doAsyncLogin(userId, pwd);
		throw new ExecAsyncException(future);
	}

	protected AsyncFuture<UserVO> doAsyncLogin(final String userId,
			final String pwd) {
		Async<UserVO> vo = context.getService(IRestProxyService.class).getRestService(StockUserResourceAsync.class,StockUserResource.class).getUser();
		AsyncFuture<UserVO> future = new AsyncFuture<UserVO>(vo){

			/* (non-Javadoc)
			 * @see com.wxxr.mobile.core.async.api.AsyncFuture#getInternalCallback()
			 */
			@Override
			public IAsyncCallback<UserVO> getInternalCallback() {
				return new DelegateCallback<UserVO, UserVO>(super.getInternalCallback()) {

					@Override
					protected UserVO getTargetValue(UserVO vo) {
						return vo;
					}

					/* (non-Javadoc)
					 * @see com.wxxr.mobile.core.async.api.DelegateCallback#failed(java.lang.Throwable)
					 */
					@Override
					public void failed(Throwable cause) {
						if(cause instanceof NotAuthorizedException){
							log.warn("Failed to login user due to invalid user name and/or password",cause);
							usernamePasswordCredential4Login = null;
							cause = new LoginFailedException("用户名或密码错误");
						}else{
							log.warn("Failed to login user due to unexpected exception",cause);
							usernamePasswordCredential4Login = null;
							cause = new LoginFailedException("登录失败，请稍后再试...");
						}
						super.failed(cause);
					}

					/* (non-Javadoc)
					 * @see com.wxxr.mobile.core.async.api.DelegateCallback#success(java.lang.Object)
					 */
					@Override
					public void success(UserVO vo) {
						if (vo!=null){
							try {
								if(myUserInfo == null){
									myUserInfo = new UserBean();
								}
								updateBindingUser(userId, pwd, vo);
								getService(IEventRouter.class).routeEvent(new UserLoginEvent(userId,LoginAction.LOGIN));
								//KUtils.getService(ITradingManagementService.class).getHomeMenuList(true);//登陆成功后按需刷新首页菜单
							}catch(Throwable t){
								super.failed(t);
								return;
							}
						}
						super.success(vo);
					}
				};
			}

		};
		return future;
	}
	
	@Override
	public void register(final String phoneNumber) throws StockAppBizException {
		UserRegisterCommand cmd=new UserRegisterCommand();
		cmd.setUserName(phoneNumber);
		AsyncUtils.execCommandAsyncInUI(context.getService(ICommandExecutor.class), cmd);
	}
	
	@Override
	public synchronized void logout(){
	   String userId = myUserInfo.getUsername();
	   saveUserBean(myUserInfo);
	   myUserInfo=null;
       getPrefManager().putPreference(getModuleName(), new Hashtable<String, String>());
       HttpRpcService httpService = context.getService(HttpRpcService.class);
       if (httpService != null) {
           httpService.resetHttpClientContext();
       }
       usernamePasswordCredential4Login=null;
       getService(IEventRouter.class).routeEvent(new UserLoginEvent(userId,LoginAction.LOGOUT));
       
       /*AsyncUtils.execRunnableAsyncInUI(new Runnable() {
   		public void run() {
   			try {
   		    	   KUtils.getService(ITradingManagementService.class).getHomeMenuList(true);//退出登陆后按需刷新首页菜单
   				} catch (Exception e) {
   					log.warn("Failed to refresh homepage after logout", e);
   				}
   			
   		}
          });*/
	}
	
	private void initBindingUser(){
	    IPreferenceManager mgr = getPrefManager();
        Dictionary<String, String> d = mgr.getPreference(getModuleName());
        String pwd = d != null ? d.get(KEY_PASSWORD) : null;
        String user_name = d != null ? d.get(KEY_USERNAME) : null;
        if(user_name != null && this.myUserInfo==null){
    		myUserInfo = new UserBean();
    		myUserInfo.setUsername(user_name);
    		loadUserBean(myUserInfo);
    		myUserInfo.setPassword(pwd);
    		usernamePasswordCredential4Login = new UsernamePasswordCredential(user_name,pwd);

        }
	}
	
	private void loadUserBean(UserBean user){
	    IPreferenceManager mgr = getPrefManager();
	    if (mgr.hasPreference(getModuleName()+"_"+user.getUsername())) {
	    	  Dictionary<String, String> d = mgr.getPreference(getModuleName()+"_"+user.getUsername());
	          if (d != null ){
	        	  if (StringUtils.isBlank(user.getUsername())) {
	        		  user.setUsername(d.get(KEY_USERNAME));
	        	  }
	        	  if (StringUtils.isBlank(user.getNickName())) {
	        		  user.setNickName(d.get(KEY_NICKNAME));
	        	  }
	        	  if (StringUtils.isBlank(user.getPhoneNumber())) {
	        		  user.setPhoneNumber(d.get(KEY_PHONENUMBER));
	        	  }
	        	  if (StringUtils.isBlank(user.getUserPic())) {
	        		  user.setUserPic(d.get(KEY_USERPIC));
	        	  }
	        	  user.setPassword(d.get(KEY_PASSWORD));
	        	  user.setHomeBack(d.get(KEY_HOME_BGIMG));
	        	  String msg_setting = d.get(KEY_MSG_SETTTING);
	        	  user.setMessagePushSettingOn("ON".equals(msg_setting));
	        	  String isBindCard = d.get(KEY_BINDCARD);
	        	  user.setBindCard("true".equalsIgnoreCase(isBindCard));
	          }
		}
	}
	
	private void saveUserBean(UserBean b){
	    Dictionary<String, String> pref = getPrefManager().getPreference(getModuleName()+"_"+b.getUsername());
        if(pref == null){
            pref= new Hashtable<String, String>();
            getPrefManager().newPreference(getModuleName()+"_"+b.getUsername(), pref);
        }else{
            pref = DictionaryUtils.clone(pref);
        }
        if (b.getNickName()!=null){
            pref.put(KEY_NICKNAME, b.getNickName());
        }
        if (b.getUsername()!=null){
            pref.put(KEY_USERNAME, b.getUsername());
        }
        if (b.getPhoneNumber()!=null){
            pref.put(KEY_PHONENUMBER,b.getPhoneNumber());
        }
        if (b.getUserPic()!=null){
            pref.put(KEY_USERPIC, b.getUserPic());
        }
        if (b.getHomeBack()!=null){
            pref.put(KEY_HOME_BGIMG, b.getHomeBack());
        }
        if (b.getMessagePushSettingOn()) {
        	   pref.put(KEY_MSG_SETTTING, "ON");
		}else{
			pref.put(KEY_MSG_SETTTING, "OFF");
		}
        if (b.getPassword()!=null) {
        	 pref.put(KEY_PASSWORD, b.getPassword());
		}
        if (b.getBindCard()) {
        	pref.put(KEY_BINDCARD,"true");
		}
        getPrefManager().putPreference(getModuleName()+"_"+b.getUsername(), pref);

	}

	
	protected IPreferenceManager getPrefManager() {
		if (this.prefManager == null) {
			this.prefManager = context.getService(IPreferenceManager.class);
		}
		return this.prefManager;
	}

	@Override
	public synchronized IUserAuthCredential getAuthCredential(String host, String realm) {
		if (usernamePasswordCredential4Login != null) {
			return usernamePasswordCredential4Login;
		}
		IPreferenceManager mgr = getPrefManager();
		if (mgr.hasPreference(getModuleName())
				&& mgr.getPreference(getModuleName()).get(KEY_USERNAME) == null) {
			
			Dictionary<String, String> d = mgr.getPreference(getModuleName());
			String userName = null;
	        String passwd = null;
			if (d!=null) {
			   userName = d.get(KEY_USERNAME);
			   passwd = d.get(KEY_PASSWORD);
	        }
			return new UsernamePasswordCredential(userName, passwd);
			//IDialog dialog = getService(IWorkbenchManager.class).getWorkbench().createDialog("userLoginPage",null );
			//dialog.show();
//			
		}
		return null;
		
	}
	@Override
	public void resetPassword(String userName) {
		RestPasswordCommand command=new RestPasswordCommand();
		command.setUserName(userName);
		AsyncUtils.execCommandAsyncInUI(context.getService(ICommandExecutor.class), command);
	}
	

	@Override
	protected void initServiceDependency() {
		addRequiredService(IEventRouter.class);
		addRequiredService(IRestProxyService.class);
		addRequiredService(IEntityLoaderRegistry.class);
		addRequiredService(ICommandExecutor.class);
	    addRequiredService(IPreferenceManager.class);	
	}

	@Override
	protected void startService() {	
		IEntityLoaderRegistry registry = getService(IEntityLoaderRegistry.class);
//		otherGainBean_cache = new GenericReloadableEntityCache<String, GainBean, List<GainBean>>("otherGainBean");
		registry.registerEntityLoader("otherGainBean", new GainBeanLoader());
		context.getService(ICommandExecutor.class).registerCommandHandler(UserRegister2Command.COMMAND_NAME, new Register2Handher());
		registry.registerEntityLoader("otherpersonalHomePageBean", new OtherPersonalHomePageLoader());
		context.registerService(IUserLoginManagementService.class, this);
		context.registerService(IUserAuthManager.class, this);
		initBindingUser();
		
	}

	@Override
	protected void stopService() {
		context.unregisterService(IUserLoginManagementService.class, this);
		context.unregisterService(IUserAuthManager.class, this);
		otherpersonalHomePageBean_cache=null;
		otherGainBean_cache=null;
	}
	
	@Override
	public String getModuleName() {
		return "UserLoginManagementService";
	}

	@Override
	public UserBean getMyUserInfo() {
		return myUserInfo;
	}
	
	private void saveCookie(String userId, String pwd) {
		  Dictionary<String, String> pref = getPrefManager().getPreference(getModuleName());
	        if(pref == null){
	            pref= new Hashtable<String, String>();
	            getPrefManager().newPreference(getModuleName(), pref);
	        }else{
	            pref = DictionaryUtils.clone(pref);
	        }
	        if (userId!=null){
	            pref.put(KEY_NICKNAME, userId);
	        }
	        if (pwd!=null){
	            pref.put(KEY_USERNAME, pwd);
	        }
	        getPrefManager().putPreference(getModuleName(), pref);
	}
//	protected PersonalHomePageBean personalHomePageBean;
	@Override
	public PersonalHomePageBean getOtherPersonalHomePage(final String userId) {
		if (otherpersonalHomePageBean_cache==null) {
			 otherpersonalHomePageBean_cache=new GenericReloadableEntityCache<String,PersonalHomePageBean,List<PersonalHomePageBean>>("otherpersonalHomePageBean");
		}
	    final String key=userId;
	    boolean forceload = false;
	    PersonalHomePageBean bean = this.otherpersonalHomePageBean_cache.getEntity(key);
		if (bean == null) {
			bean = new PersonalHomePageBean();
			otherpersonalHomePageBean_cache.putEntity(key, bean);
			forceload = true;
		}
//        if (otherpersonalHomePageBean_cache.getEntity(key)==null){
//            PersonalHomePageBean b=new PersonalHomePageBean();
//            otherpersonalHomePageBean_cache.putEntity(key,b);
//        }
        Map<String, Object> p=new HashMap<String, Object>(); 
        p.put("userId", userId);
        if(forceload){
	        AsyncUtils.forceLoadNFetchAsyncInUI(this.otherpersonalHomePageBean_cache, p, new AsyncFuture<PersonalHomePageBean>(), new IEntityFetcher<PersonalHomePageBean>() {
	
				@Override
				public PersonalHomePageBean fetchFromCache(
						IBindableEntityCache<?, ?> cache) {
					return (PersonalHomePageBean)cache.getEntity(key);
				}
			});
        }else{
        	 this.otherpersonalHomePageBean_cache.doReload(true, p, null);
        }
        return bean;
	}

    public BindableListWrapper<GainBean> getMoreOtherPersonal(final String userId, int start,int limit, final boolean virtual) {
    	if (otherGainBean_cache==null) {
    		otherGainBean_cache = new GenericReloadableEntityCache<String, GainBean, List<GainBean>>("otherGainBean"){
    			@Override
				protected Map<String, Object> prepareLoadmoreCommandParameter(
						BindableListWrapper<GainBean> list) {
					Map<String, Object> params=new HashMap<String, Object>();
					int start = otherGainBean_cache.getCacheSize();
					params.put("virtual", virtual);
					params.put("start", start);
					params.put("limit", 20);
					params.put("userId", userId);
					return params;
				}
    		};
		}
    	//if (otherGainBeans==null) {
    	final BindableListWrapper<GainBean> otherGainBeans = otherGainBean_cache.getEntities(new IEntityFilter<GainBean>(){
                @Override
                public boolean doFilter(GainBean entity) {
                    if ( entity.getUserId().equals(userId) && entity.getVirtual()==virtual ){
                        return true;
                    }
                    return false;
                }
            }, viewMoreComparator);
		//}
      Map<String, Object> p=new HashMap<String, Object>(); 
      p.put("virtual", virtual);
      p.put("start", start);
      p.put("limit", limit);
      p.put("userId", userId);
      otherGainBean_cache.setCommandParameters(p);
      otherGainBeans.setReloadParameters(p);
      return AsyncUtils.forceLoadNFetchAsyncInUI(otherGainBean_cache,p, new AsyncFuture<BindableListWrapper<GainBean>>(),
  			new IEntityFetcher<BindableListWrapper<GainBean>>() {

					@Override
					public BindableListWrapper<GainBean> fetchFromCache(
							IBindableEntityCache<?, ?> cache) {
						return otherGainBeans;
					}
				});
    }
    private static Comparator<GainBean> viewMoreComparator = new Comparator<GainBean>() {
		@Override
		public int compare(GainBean o1, GainBean o2) {
			if (o2!=null&&o1!=null) {
				return (o2.getTradingAccountId()-o1.getTradingAccountId())>0?1:-1;
			}
			return 0;
			//return o2.getCloseTime().compareTo(o1.getCloseTime());
		}
	};
	
	private void scheduleLoginVerification(int delay, TimeUnit unit) {
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				if(myUserInfo != null){
					getService(IEventRouter.class).routeEvent(new UserLoginEvent(myUserInfo.getUsername(),LoginAction.LOGIN));		
					Async<UserVO> vo = context.getService(IRestProxyService.class).getRestService(StockUserResourceAsync.class,StockUserResource.class).getUser();
					vo.onResult(new IAsyncCallback<UserVO>() {

						@Override
						public void cancelled() {
							log.warn("Verfication of binding user was cancelled, will retry later");
							scheduleLoginVerification(5,TimeUnit.SECONDS);
						}

						@Override
						public void failed(Throwable cause) {
							if(cause instanceof NotAuthorizedException){
								log.warn("Failed to login user due to invalid user name and/or password",cause);
								usernamePasswordCredential4Login = null;
								logout();
							}else{
								log.warn("Failed to login user due to unexpected exceptionm will retry later",cause);
								scheduleLoginVerification(5,TimeUnit.SECONDS);
							}
						}

						@Override
						public void setCancellable(ICancellable arg0) {
						}

						@Override
						public void success(UserVO vo) {
							updateBindingUser(usernamePasswordCredential4Login.getUserName(), usernamePasswordCredential4Login.getAuthPassword(), vo);
						}
					});
				}
			}
		};
		if(delay <= 0){
			KUtils.invokeLater(task);
		}else{
			KUtils.invokeLater(task, delay, unit);
		}
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#onKernelStarted()
	 */
	@Override
	protected void onKernelStarted() {
		if(this.myUserInfo != null){
			getService(IEventRouter.class).routeEvent(new UserLoginEvent(this.myUserInfo.getUsername(),LoginAction.LOGIN));
			scheduleLoginVerification(200, TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * @param userId
	 * @param pwd
	 * @param vo
	 */
	public void updateBindingUser(final String userId, final String pwd,
			UserVO vo) {
		myUserInfo.setNickName(vo.getNickName());
		myUserInfo.setUsername(userId);
		myUserInfo.setPhoneNumber(vo.getMoblie());
		myUserInfo.setUserPic(vo.getIcon());
		saveCookie(userId,pwd);
		loadUserBean(myUserInfo);
		myUserInfo.setPassword(pwd);
		//根据用户密码登录成功
		Dictionary<String, String> pref = getPrefManager().getPreference(getModuleName());
		if(pref == null){
			pref= new Hashtable<String, String>();
			getPrefManager().newPreference(getModuleName(), pref);
		}else{
			pref = DictionaryUtils.clone(pref);
		}
		pref.put(KEY_USERNAME, userId);
		pref.put(KEY_PASSWORD, pwd);
		pref.put(KEY_UPDATE_DATE, String.valueOf(System.currentTimeMillis()));
		getPrefManager().putPreference(getModuleName(), pref);
	}
	
	@Override
	public void register(String userName, String pass, String pass2)
			throws StockAppBizException {
		if (StringUtils.isBlank(userName)||StringUtils.isBlank(pass)) {
			throw new StockAppBizException("用户名或密码不能为空");
		}
		if (!pass.equals(pass2)) {
			throw new StockAppBizException("两次输入密码不一致");
		}
		UserRegister2Command cmd=new UserRegister2Command();
		cmd.setUserName(userName);
		cmd.setPassword(pass);
		AsyncUtils.execCommandAsyncInUI(cmd,new IDataConverter<SimpleResultVo, Object>(){

			@Override
			public Object convert(SimpleResultVo vo)
					throws NestedRuntimeException {
				if(vo.getResult()!=0){
					throw new NestedRuntimeException(new StockAppBizException(vo.getMessage()));
				}
				return null;
			}
			
		});
		
	}
	
}
