/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wxxr.javax.ws.rs.NotAuthorizedException;
import com.wxxr.mobile.android.preference.DictionaryUtils;
import com.wxxr.mobile.core.api.IUserAuthCredential;
import com.wxxr.mobile.core.api.IUserAuthManager;
import com.wxxr.mobile.core.api.UsernamePasswordCredential;
import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.event.api.IEventRouter;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.HttpRpcService;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.security.api.LoginAction;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.preference.api.IPreferenceManager;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.LoginFailedException;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.event.UserLoginEvent;
import com.wxxr.mobile.stock.app.service.IUserLoginManagementService;
import com.wxxr.mobile.stock.app.service.handler.RegisterHandher.UserRegisterCommand;
import com.wxxr.mobile.stock.app.service.handler.RestPasswordHandler.RestPasswordCommand;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;
import com.wxxr.stock.restful.resource.StockUserResource;

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
	
	@Override
	public  void login(final String userId, final String pwd) throws LoginFailedException {
		Future<?> future = context.getExecutor().submit(new Runnable() {
			@Override
			public void run() {
                usernamePasswordCredential4Login = new UsernamePasswordCredential(
                        userId, pwd);
				try {
					UserVO vo = context.getService(IRestProxyService.class).getRestService(StockUserResource.class).getUser();
					if (vo!=null){
    					myUserInfo = new UserBean();
    					myUserInfo.setNickName(vo.getNickName());
    					myUserInfo.setUsername(vo.getUserName());
    					myUserInfo.setPhoneNumber(vo.getMoblie());
    					myUserInfo.setUserPic(vo.getIcon());
    					saveCookie(userId,pwd);
    					restoreUserBean(myUserInfo);
    					myUserInfo.setPassword(pwd);
    					getService(IEventRouter.class).routeEvent(new UserLoginEvent(userId,LoginAction.LOGIN));
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
					
				} catch (NotAuthorizedException e) {
					log.warn("Failed to login user due to invalid user name and/or password",e);
					usernamePasswordCredential4Login = null;
					throw new LoginFailedException("用户名或密码错误");
				} catch (Throwable e) {
					log.warn("Failed to login user due to unexpected exception",e);
					usernamePasswordCredential4Login = null;
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
	public void register(final String phoneNumber) throws StockAppBizException {
		UserRegisterCommand cmd=new UserRegisterCommand();
		cmd.setUserName(phoneNumber);
		try{
			Future<SimpleResultVo> future=context.getService(ICommandExecutor.class).submitCommand(cmd);
				try {
					SimpleResultVo vo=future.get(30,TimeUnit.SECONDS);
					if(vo.getResult()!=0){
						throw new StockAppBizException(vo.getMessage());
					}
				} catch (Exception e) {
					throw new StockAppBizException(e.getMessage());
				}
			}catch(CommandException e){
				throw new StockAppBizException(e.getMessage());
			}

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
		}
        if (b.getPassword()!=null) {
        	 pref.put(KEY_PASSWORD, b.getPassword());
		}
        if (b.getBindCard()) {
        	pref.put(KEY_BINDCARD,"true");
		}
        getPrefManager().putPreference(getModuleName()+"_"+b.getUsername(), pref);

	}
	private void loadCookie(){
	    IPreferenceManager mgr = getPrefManager();
        Dictionary<String, String> d = mgr.getPreference(getModuleName());
        String pwd = d != null ? d.get(KEY_PASSWORD) : null;
        String user_name = d != null ? d.get(KEY_USERNAME) : null;
        if(user_name != null && this.myUserInfo==null){
        	try {
				login(user_name, pwd);
			} catch (Exception e) {
				log.warn("Failed to login by cookie",e);
			}
        }
	}
	private void restoreUserBean(UserBean user){
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
	        	  user.setMessagePushSettingOn("true".equalsIgnoreCase(isBindCard));
	          }
		}
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
		try{
			Future<Void> future=context.getService(ICommandExecutor.class).submitCommand(command);
				try {
					future.get(30,TimeUnit.SECONDS);
				} catch (Exception e) {
					throw new StockAppBizException("系统错误");
				}
			}catch(CommandException e){
				throw new StockAppBizException(e.getMessage());
			}
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
		context.registerService(IUserLoginManagementService.class, this);
		context.registerService(IUserAuthManager.class, this);
		loadCookie();
	}

	@Override
	protected void stopService() {
		context.unregisterService(IUserLoginManagementService.class, this);
		context.unregisterService(IUserAuthManager.class, this);
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
}
