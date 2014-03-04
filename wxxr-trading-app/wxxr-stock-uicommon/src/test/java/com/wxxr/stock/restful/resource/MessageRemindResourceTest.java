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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;

import junit.framework.TestCase;

import com.wxxr.mobile.core.api.IUserAuthCredential;
import com.wxxr.mobile.core.api.IUserAuthManager;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.apache.AbstractHttpRpcService;
import com.wxxr.mobile.core.rpc.http.api.HttpRequest;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;
import com.wxxr.mobile.stock.app.MockApplication;
import com.wxxr.mobile.stock.app.MockRestClient;
import com.wxxr.stock.notification.ejb.api.MsgQuery;
import com.wxxr.stock.restful.json.MessageVOs;


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
		messageRemindResource=builder.getRestService(IMessageRemindResource.class,null,"http://192.168.123.44:8480/mobilestock2");
	}
	public void testFindById()throws Exception{
		MsgQuery vo = new MsgQuery();
//		vo.setId("whatever");
		List<String> list = Collections.synchronizedList(new ArrayList<String>());
		MessageVOs a =messageRemindResource.findById(vo);
		System.out.println(a);
	}
}
