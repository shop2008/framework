/**
 * 
 */
package com.wxxr.mobile.web.grabber.test;

import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;

import com.wxxr.mobile.core.security.api.ISiteSecurityService;
import com.wxxr.mobile.web.grabber.common.AbstractGrabberModule;

/**
 * @author neillin
 *
 */
public class TestSiteSecurityService extends AbstractGrabberModule implements
		ISiteSecurityService {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.security.api.ISiteSecurityService#getHostnameVerifier()
	 */
	@Override
	public HostnameVerifier getHostnameVerifier() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.security.api.ISiteSecurityService#getTrustKeyStore()
	 */
	@Override
	public KeyStore getTrustKeyStore() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.security.api.ISiteSecurityService#getSiteKeyStore()
	 */
	@Override
	public KeyStore getSiteKeyStore() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#initServiceDependency()
	 */
	@Override
	protected void initServiceDependency() {

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#startService()
	 */
	@Override
	protected void startService() {
		context.registerService(ISiteSecurityService.class, this);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#stopService()
	 */
	@Override
	protected void stopService() {
		context.unregisterService(ISiteSecurityService.class, this);
	}

}
