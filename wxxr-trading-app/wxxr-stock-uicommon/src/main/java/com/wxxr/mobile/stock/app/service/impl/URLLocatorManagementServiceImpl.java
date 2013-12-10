/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.preference.api.IPreferenceManager;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.service.IURLLocatorManagementService;
import com.wxxr.stock.restful.resource.IURLLocatorResource;

/**
 * @author wangxuyang
 * 
 */
public class URLLocatorManagementServiceImpl extends AbstractModule<IStockAppContext> implements
		IURLLocatorManagementService {
	private static final Trace log = Trace.register(URLLocatorManagementServiceImpl.class);
	private long lastCheckTime;
	private int checkIntervalInSeconds = 30*60;		// 30 minutes
	private String serverUrl;
	private String magnoliaUrl;
	private Dictionary<String, String> defaultSettings = new Hashtable<String, String>();
	private Timer timer;
	Map<String,String> remoteConfig;
	@Override
	public String getServerURL() {
		return serverUrl;
	}

	@Override
	public String getMagnoliaURL() {
		return magnoliaUrl;
	}

	@Override
	protected void initServiceDependency() {
		addRequiredService(IPreferenceManager.class);
	}

	@Override
	protected void startService() {
		if (log.isDebugEnabled()) {
			log.debug("Loading local settings...");
		}
		loadLocalSettings();
		this.serverUrl = getURL("server");
		this.magnoliaUrl = getURL("magnolia");
		if (log.isDebugEnabled()) {
			log.debug("Local settings:"+defaultSettings);
		}
		timer = new Timer("Remote Settings Check Thread");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(((System.currentTimeMillis() - lastCheckTime) >= checkIntervalInSeconds*1000L)||context.getApplication().isInDebugMode()){
					try {
						loadRemoteSettings();
					} catch (Throwable e) {
							log.warn("Error when get remote settings", e);
					}
					lastCheckTime = System.currentTimeMillis();
				}
			}
		}, 10000,10000);
		context.registerService(IURLLocatorManagementService.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(IURLLocatorManagementService.class,this);
	}

	private void loadLocalSettings() {
		InputStream in = null;
		try {
			in = context.getApplication().getAndroidApplication().getAssets()
					.open("server.properties");
			Properties props = new Properties();
			props.load(in);
			if (props.size() > 0) {
				Set<Object> keys = props.keySet();
				for (Object obj : keys) {
					String key = (String) obj;
					defaultSettings.put(key, props.getProperty(key));
				}
			}
		} catch (IOException e) {
			log.warn("Error when open file:server.properties", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

				}
			}
		}
	}
	private void loadRemoteSettings() {
		try {
			String digest = null;
			byte[] data = context.getService(IRestProxyService.class).getRestService(IURLLocatorResource.class).isServerConfigChanged(digest);
			if (data==null) {
				return;
			}
			remoteConfig = fromBytes(data);
			IPreferenceManager prefManager = context.getService(IPreferenceManager.class);	
			String prefName = getModuleName();
			if (!prefManager.hasPreference(prefName)) {
				Dictionary<String, String> d = new Hashtable<String, String>();
				prefManager.newPreference(prefName, d);
			}
			String sUrl =  null;
			String mUrl =  null;
			if (StringUtils.isNotBlank(sUrl)) {
				this.serverUrl = sUrl;
				if (isChanged(getModuleName(), "server", sUrl)) {
					prefManager.updatePreference(prefName, "server", sUrl);
				}
			}
			if (StringUtils.isNotBlank(mUrl)) {
				this.magnoliaUrl = mUrl;
				if (isChanged(getModuleName(), "magnolia", mUrl)) {
					prefManager.updatePreference(prefName, "magnolia", mUrl);
				}
			}
			if (context.getApplication().isInDebugMode()) {
				sUrl = remoteConfig.get("test.server");
				mUrl = remoteConfig.get("test.maganoliaURL");
			}else{
				sUrl = remoteConfig.get("product.server");
				mUrl = remoteConfig.get("product.maganoliaURL");
			}
		} catch (Exception e) {
			log.warn("Error when open file:server.properties", e);
		}
	}
	private <T> T fromBytes(byte[] data) throws Exception {
		if (data == null) {
			return null;
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bis);
			return (T)ois.readObject();
		} catch (Exception e) {
			log.warn("Error when data info from bytes", e);
			throw e;
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
				bis.close();
			} catch (IOException e) {
			}
		}

	}
	public String getURL(String name) {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("The attribute name is null!!!");
		}
		String value = null;
		String pid = getModuleName();
		if (context.getService(IPreferenceManager.class).hasPreference(pid,name)) {
			value = context.getService(IPreferenceManager.class).getPreference(pid, name);
			if (value != null) {
				return value;
			}
		}
		value = defaultSettings.get(name);
		return value;
	}
	boolean isChanged(String pid,String name,String new_value){
		String old_value = context.getService(IPreferenceManager.class).getPreference(pid, name);
		if (old_value==null) {
			return new_value!=null;
		}
		return !old_value.equals(new_value);
	}
	@Override
	public String getModuleName() {
		return "URLManagement";
	}
}
