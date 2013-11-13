/*
 * @(#)IProtocolStack.java	 2011-8-25
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.stock.security.impl;

import java.io.IOException;

import com.wxxr.mobile.stock.security.IConnectorContext;
import com.wxxr.mobile.stock.security.IProtocol;

public class ProtocolStack implements IProtocol {

	private ProtocolStack s;
	private AbstractProtocol topProtocol;
	private HttpClientProtocol httpProtocol;
	private LoginProtocol login;
	private EnDeCodeProtocol endecode;
	public void init(IConnectorContext context) {
		if (topProtocol == null) {
			 httpProtocol = new HttpClientProtocol(context);
			 login = new LoginProtocol(context);

			if (context.hasEnDeCodeProtocol()){
				endecode = new EnDeCodeProtocol(context);
				login.setNext(endecode);
				endecode.setNext(httpProtocol);
			}else{
				login.setNext(httpProtocol);
			}
			
			topProtocol = login;
		}
	}

	@Override
	public HttpResponse service(HttpRequest request) throws IOException {
		return getNext().service(request);
	}

	@Override
	public IProtocol getNext() {
		return topProtocol;
	}
	public boolean logout() {
		if (endecode!=null){
			endecode.setState(null);
		}
		return true;
	}
}
