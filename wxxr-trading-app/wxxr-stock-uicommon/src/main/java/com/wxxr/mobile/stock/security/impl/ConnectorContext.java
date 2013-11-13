/*
 * @(#)ConnectorContext.java	 2011-10-27
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.stock.security.impl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.preference.api.IPreferenceManager;
import com.wxxr.mobile.stock.client.IStockAppContext;
import com.wxxr.mobile.stock.security.IConnectorContext;
import com.wxxr.mobile.stock.security.IExceptionHandler;
import com.wxxr.mobile.stock.security.ILoginView;
import com.wxxr.mobile.stock.security.ProtocolCommon;

public class ConnectorContext implements IConnectorContext {
	private static final Trace log = Trace.register(ConnectorContext.class);
	private static String apk_licence = "2011.11.25.000";
	private static boolean hasEnDeCodeProtocol = true;// 安全通道的开关
	private IStockAppContext context;
	private ILoginView loginView;
	private IExceptionHandler handler;
	private String serverURl;

	public ConnectorContext(IStockAppContext context) {
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		}
		this.context = context;
	}

	public String getDeviceId() {
		return context.getApplication().getDeviceId();
	}

	public String getVersionId() {
		return context.getApplication().getApplicationVersion();

	}

	@Override
	public String getUrlPrefix() {
		if (StringUtils.isBlank(serverURl)) {
			serverURl = context.getService(IPreferenceManager.class).getPreference(IPreferenceManager.SYSTEM_PREFERENCE_NAME, IPreferenceManager.SYSTEM_PREFERENCE_KEY_DEFAULT_SERVER_URL);
		}
		return serverURl;
	}

	@Override
	public String getLicence() {
		return apk_licence;
	}

	@Override
	public String getProductId() {
		return context.getApplication().getApplicationId();
	}

	public void put(String key, String value) {
		context.setAttribute(key, value);
	}

	public String get(String key) {
		return (String) context.getAttribute(key);
	}

	public void remove(String key) {
		context.removeAttribute(key);
	}

	@Override
	public IExceptionHandler getExceptionHandler() {
		if (this.handler == null) {
			handler = new ExceptionHandlerImpl();
		}
		return handler;
	}

	public InputStream getCientCert() {
		try {
			return context.getApplication().getAndroidApplication().getAssets().open("client.p12");
		} catch (IOException e) {
			log.error("Error when opening server.cer", e);
		}
		return null;
	}

	public InputStream getServerCert() {
		try {
			return context.getApplication().getAndroidApplication().getAssets().open("server.cer");
		} catch (IOException e) {
			log.error("Error when opening server.cer", e);
		}
		return null;
	}

	@Override
	public String getLoginUserNameType() {
		return ProtocolCommon.USER_NAME_TYPE_DEVICEID;
	}

	@Override
	public ILoginView getLoginview() {
		return loginView;
	}

	@Override
	public boolean hasEnDeCodeProtocol() {
		return hasEnDeCodeProtocol;
	}

}
