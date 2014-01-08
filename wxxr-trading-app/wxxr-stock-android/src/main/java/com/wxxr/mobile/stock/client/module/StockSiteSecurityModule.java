/**
 * 
 */
package com.wxxr.mobile.stock.client.module;

import java.io.InputStream;
import java.security.KeyStore;

import com.wxxr.mobile.android.security.DummySiteSecurityModule;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;
import com.wxxr.mobile.stock.app.IStockAppContext;

/**
 * @author wangxuyang
 *
 */
public class StockSiteSecurityModule extends DummySiteSecurityModule<IStockAppContext> {
	private static final Trace log = Trace.register(StockSiteSecurityModule.class);
	private KeyStore trustKeyStore;
	@Override
	protected void stopService() {
		this.context.unregisterService(ISiteSecurityService.class, this);
	}
	@Override
	protected void startService() {
		try {
			trustKeyStore = KeyStore.getInstance("JKS");
			InputStream in =context.getApplication().getAndroidApplication().getAssets().open("trust.keystore");
			trustKeyStore.load(in,   "111111".toCharArray());
		} catch (Exception e) {
			log.warn("Failed to load trust key store",e);
		}
		context.registerService(ISiteSecurityService.class, this);
	}
	@Override
	public KeyStore getTrustKeyStore() {
		return trustKeyStore;
	}
}
