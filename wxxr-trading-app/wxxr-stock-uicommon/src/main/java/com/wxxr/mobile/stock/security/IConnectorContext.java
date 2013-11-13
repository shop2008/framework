/*
 * @(#)IConnectorContext.java	 2011-10-27
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.stock.security;

import java.io.InputStream;

public interface IConnectorContext
{

	public boolean hasEnDeCodeProtocol();
	
	/**
	 * @return
	 */
	public String getDeviceId();
	public String getVersionId();

	/**
	 * @return
	 */
	public String getLicence();

	public String getUrlPrefix();

	/**
	 * @return
	 */
	public String getProductId();

	/**
	 * @return
	 */
	public void put(String key, String value);

	/**
	 * @return
	 */
	public String get(String key);

	public void remove(String key);

	public ILoginView getLoginview();


	public String getLoginUserNameType();

	/**
	 * @return
	 */
	public IExceptionHandler getExceptionHandler();

	/**
	 * @return
	 */
	public InputStream getCientCert();

	/**
	 * @return
	 */
	public InputStream getServerCert();

}
