package com.wxxr.mobile.stock.security.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.security.auth.login.LoginException;

import com.wxxr.mobile.stock.client.util.StringUtils;
import com.wxxr.mobile.stock.security.ICallBack;
import com.wxxr.mobile.stock.security.IConnector;
import com.wxxr.mobile.stock.security.IConnectorContext;
import com.wxxr.mobile.stock.security.IHttpResponse;
import com.wxxr.mobile.stock.security.ILoginContext;
import com.wxxr.mobile.stock.security.ProtocolCommon;

public class Connector implements IConnector,ILoginContext{
	Logger log=Logger.getLogger(Connector.class);

	private static Connector connector;
	
	public static IConnector getInstance(){
		return connector;
	}
	public static ILoginContext getLoginContext(){
		return connector;
	}
	public static IConnector createConnector(IConnectorContext context){
		if (connector==null){
			connector=new Connector(context);
		}
		return connector;
	}
	private int maxThreads = 5;
	private ExecutorService executor;
	private ProtocolStack s;
	private IConnectorContext context;
	public Connector(IConnectorContext context){
		s=new ProtocolStack();
		s.init(context);
		this.context=context;
	}
	protected ExecutorService getExecutor() {
		if (executor == null) {
			executor = new ThreadPoolExecutor(1, this.maxThreads, 60,
					TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
					new ThreadFactory() {
						private AtomicInteger seqNO = new AtomicInteger();

						@Override
						public Thread newThread(Runnable r) {
							return new Thread(r,
									"Mobile Stock client Thread -- "
											+ seqNO.incrementAndGet());
						}
					}, new ThreadPoolExecutor.CallerRunsPolicy());
		}
		return executor;
	}
	public void service(HttpRequest request,ICallBack callback) {
		HttpRequestRunable r=new HttpRequestRunable(request,callback);
		getExecutor().submit(r);
	}
	public IHttpResponse service(HttpRequest request) {
		try {
			HttpRequest bak=request.clone();
			HttpResponse r= s.service(request);
			
			if (r.getStatus() == HttpURLConnection.HTTP_UNAUTHORIZED){
				showLoginView();
				r=s.service(bak);
			}
			return r;
		} catch (IOException e) {
			context.getExceptionHandler().handle(e);
			log.error(e.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	public void showLoginView(){
		this.context.getLoginview().showLoginView(this);
	}
	public   class HttpRequestRunable implements Runnable {
		HttpRequestRunable(HttpRequest request,ICallBack callback){
			r=request;
			cb=callback;
		}
		HttpRequest r;
		ICallBack cb;
		@Override
		public void run() {
			HttpResponse response=null;
			try {
				response=s.service(r);
				if (response.getStatus() == HttpURLConnection.HTTP_UNAUTHORIZED){
					showLoginView();
					response=s.service(r);
				}
				if ((response.getStatus()==200 && response.getHeader("BizException")==null) ||(response.getStatus()==204 && response.getHeader("BizException")==null) ){
					cb.onSuccess(response);
				}else{
					cb.onFailed(response);
				}
			} catch (IOException e) {
					cb.onFailed(response);
			}catch(Exception e){
					cb.onFailed(response);
			}
		}
		
	}
	@Override
	public boolean login(String userName, String passwd) throws LoginException {
		try {
			context.put(ProtocolCommon.USERNAME_KEY, userName);
			context.put(ProtocolCommon.PASSWD_KEY, passwd);
			HttpResponse r=s.service( new HttpGetRequest("/rest/user/info"));
			if(200==r.getStatus()){
				context.put(ProtocolCommon.LOGIN_STATUS_KEY, ProtocolCommon.LOGIN_STATUS_SUCESSED);
				return true;
			}else{
				context.remove(ProtocolCommon.USERNAME_KEY);
				context.remove(ProtocolCommon.PASSWD_KEY);
				String e=r.getHeader("LoginException");
				if (StringUtils.isNotBlank(e)){
					throw new LoginException(e);
				}else{
					throw new LoginException("LoginError");
				}
			}
		} catch (IOException e) {
			throw new LoginException("LoginError");
		}
	}
	@Override
	public boolean islogin() {
		boolean status=false;
		if (ProtocolCommon.LOGIN_STATUS_SUCESSED.equals(context.get(ProtocolCommon.LOGIN_STATUS_KEY))){
			status=true;
		}
		return status;
	}
	
	public boolean logout() {
		context.remove(ProtocolCommon.USERNAME_KEY);
		context.remove(ProtocolCommon.PASSWD_KEY);
		context.remove(ProtocolCommon.LOGIN_STATUS_KEY);
		s.logout();
		return true;
	}
}
