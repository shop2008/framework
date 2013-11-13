package com.wxxr.mobile.stock.security;

import javax.security.auth.login.LoginException;

import com.wxxr.mobile.stock.security.impl.HttpRequest;

public interface IConnector {
		//异步
		public void service(HttpRequest request,ICallBack callback);
		//ͬ同步
		public IHttpResponse service(HttpRequest request);
		//登录
		public boolean login(String userName,String passwd) throws LoginException;
		
		public boolean islogin();
		
		public boolean logout();

}
