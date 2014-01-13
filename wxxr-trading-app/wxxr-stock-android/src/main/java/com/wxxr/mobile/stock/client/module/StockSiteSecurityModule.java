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
 * SiteSecurityModule
 * android 系统中使用的证书要求以BKS的库文件结构保存，
 * 通常情况下，我们使用java的keytool只能生成jks的证书库，如果生成BKS的则需要下载BC库，
 * 如是JDK1.6则下载bcprov-jdk16-141.jar，且将该文件放到jdk1.6.0_03\jre\lib\ext目录下，
 * 然后运行以下命令即可以生成BKS的证书库和相应的证书。
 * keytool -importcert -file CA证书文件  -keystore 库文件名称  -storetype BKS -provider org.bouncycastle.jce.provider.BouncyCastleProvider
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
			trustKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());//默认类型为bks,需要生成bks格式的keystore
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
