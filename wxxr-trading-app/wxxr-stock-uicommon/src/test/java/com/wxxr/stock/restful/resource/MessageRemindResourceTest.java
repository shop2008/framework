/*
 * @(#)MessageRemindResource.java	 2012-3-28
 *
 * Copyright 2004-2012 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.stock.restful.resource;

import java.security.KeyStore;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;

import junit.framework.TestCase;

import com.wxxr.mobile.core.api.IUserAuthCredential;
import com.wxxr.mobile.core.api.IUserAuthManager;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.apache.AbstractHttpRpcService;
import com.wxxr.mobile.core.rpc.http.api.HttpHeaderNames;
import com.wxxr.mobile.core.rpc.http.api.HttpRequest;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.rpc.rest.ResteasyRestClientService;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;
import com.wxxr.mobile.stock.app.MockApplication;
import com.wxxr.mobile.stock.app.MockRestClient;
import com.wxxr.stock.notification.ejb.api.MessageVO;
import com.wxxr.stock.notification.ejb.api.MsgQuery;


public class MessageRemindResourceTest extends TestCase{

	private IMessageRemindResource messageRemindResource;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		init();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		messageRemindResource=null;
	}
	protected void init() {
		AbstractHttpRpcService service = new AbstractHttpRpcService(){
			@Override
			public HttpRequest createRequest(String endpointUrl, Map<String, Object> params) {
				HttpRequest request=super.createRequest(endpointUrl, params);
				request.setHeader("deviceid", "123123123123123123");
				request.setHeader("deviceType", "Android");
				request.setHeader("appName", "trading");
				request.setHeader("appVer", "1.0");

				return request;
			}
		};
		service.setEnablegzip(false);
		MockApplication app = new MockApplication(){
			ExecutorService executor = Executors.newFixedThreadPool(3);

			@Override
			public ExecutorService getExecutorService() {
				return executor;
			}
			
			@Override
			protected void initModules() {
				
			}
			
		};
		IKernelContext context = app.getContext();
		context.registerService(IUserAuthManager.class, new IUserAuthManager() {
			@Override
			public IUserAuthCredential getAuthCredential(String host,
					String realm) {
				return new IUserAuthCredential() {
					
					@Override
					public String getUserName() {
						return "13810212581";
					}
					
					@Override
					public String getAuthPassword() {
						return "939906";
					}

				};
			}
		});
		service.startup(context);
		context.registerService(ISiteSecurityService.class, new ISiteSecurityService() {
			
			@Override
			public KeyStore getTrustKeyStore() {
				return null;
			}
						
			@Override
			public KeyStore getSiteKeyStore() {
				return null;
			}
			
			@Override
			public HostnameVerifier getHostnameVerifier() {
				return null;
			}
		});
		
		MockRestClient builder = new MockRestClient();
		builder.init(context);
		messageRemindResource=builder.getRestService(IMessageRemindResource.class,"http://192.168.123.44:8480/mobilestock2");
	}
	public void testFindById()throws Exception{
		MsgQuery vo = new MsgQuery();
//		vo.setId("whatever");
		List<MessageVO> a =messageRemindResource.findById(vo);
		System.out.println(a);
	}
}
