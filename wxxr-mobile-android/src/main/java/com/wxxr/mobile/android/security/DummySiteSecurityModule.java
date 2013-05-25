package com.wxxr.mobile.android.security;

import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;

public class DummySiteSecurityModule<T extends IAndroidAppContext> extends
		AbstractModule<T> implements ISiteSecurityService{

	@Override
	protected void initServiceDependency() {
	}

	@Override
	protected void startService() {
		this.context.registerService(ISiteSecurityService.class, this);
	}

	@Override
	protected void stopService() {
		this.context.unregisterService(ISiteSecurityService.class, this);
	}

	@Override
	public SSLContext getSslContext(String protocol) {
		return null;
	}

	@Override
	public HostnameVerifier getHostnameVerifier() {
		return null;
	}

	@Override
	public KeyStore getTrustKeyStore() {
		return null;
	}

	@Override
	public KeyStore getSiteKeyStore() {
		return null;
	}

}
