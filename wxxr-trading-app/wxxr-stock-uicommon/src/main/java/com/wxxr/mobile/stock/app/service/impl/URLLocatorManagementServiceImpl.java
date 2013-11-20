/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.preference.api.IPreferenceManager;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.service.IURLLocatorManagementService;

/**
 * @author wangxuyang
 * 
 */
public class URLLocatorManagementServiceImpl extends
		AbstractModule<IStockAppContext> implements
		IURLLocatorManagementService {
	private static final Trace log = Trace
			.register(URLLocatorManagementServiceImpl.class);
	private String serverUrl;
	private String magnoliaUrl;
	private Dictionary<String, String> defaultSettings = new Hashtable<String, String>();

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
	@Override
	public String getModuleName() {
		return "URLManagement";
	}
}
