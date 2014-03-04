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
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.util.Md5;
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
	private static final Trace log = Trace.register("com.wxxr.mobile.stock.app.service.impl.URLLocatorManagementServiceImpl");
	private final static String  RELATIVE_URI_APPLICATION ="/mobilestock2";
	private final static String  RELATIVE_URI_MAGNOLIA ="/magnoliaPublic";
	private final static String PREF_KEY_DIGEST = "_digest";
	private final static String PREF_KEY_SERVER_URL = "server";
	private final static String PREF_KEY_MAGNOLIA_URL = "magnolia";
	private final static String PREF_KEY_LOCATOR_SERVER = "l_server";
	
	private int checkIntervalInSeconds = 30*60;		// 30 minutes
	
	private Dictionary<String, String> currentSetting;
	private boolean started = false;
	private CountDownLatch latch;
	
	private Runnable reloadTask = new Runnable() {
		
		@Override
		public void run() {
			if(!started){
				return;
			}
			updateFromRemote();
			if(started) {
				context.getService(ICommandExecutor.class).invokeLater(reloadTask, checkIntervalInSeconds, TimeUnit.SECONDS);
			}
		}
	}; 
	
	@Override
	public String getServerURL() {
		return getURL(PREF_KEY_SERVER_URL)+RELATIVE_URI_APPLICATION;
	}

	@Override
	public String getMagnoliaURL() {
		String magnoliaUrl = getURL(PREF_KEY_MAGNOLIA_URL);
		if (StringUtils.isBlank(magnoliaUrl)) {
			magnoliaUrl = getServerURL();
		}
		return magnoliaUrl;
	}

	@Override
	protected void initServiceDependency() {
		addRequiredService(IPreferenceManager.class);
		addRequiredService(ICommandExecutor.class);
	}

	@Override
	protected void startService() {
		if (log.isDebugEnabled()) {
			log.debug("Loading local settings...");
		}
		loadInSettings();
		
		if (log.isDebugEnabled()) {
			log.debug("Local settings:"+currentSetting);
		}
		context.registerService(IURLLocatorManagementService.class, this);
		this.started = true;
		this.latch = new CountDownLatch(1);
		context.getService(ICommandExecutor.class).invokeLater(reloadTask, 1L, TimeUnit.MILLISECONDS);
	}

	@Override
	protected void stopService() {
		this.started = false;
		context.unregisterService(IURLLocatorManagementService.class,this);
	}

	private void loadInSettings() {
		IPreferenceManager prefMgr = context.getService(IPreferenceManager.class);
		currentSetting = prefMgr.getPreference(getModuleName());
		if(currentSetting == null){
			//加载出厂设置
			if(log.isInfoEnabled()){
				log.info("Load in factory setting from server.properties file ...");
			}
			currentSetting = new Hashtable<String, String>();
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
						currentSetting.put(key, props.getProperty(key));
					}
				}
				currentSetting.put(PREF_KEY_LOCATOR_SERVER,getServerURL());
				prefMgr.newPreference(getModuleName(), currentSetting);
				currentSetting = prefMgr.getPreference(getModuleName());
			} catch (Throwable e) {
				log.warn("Caught throwable when loading url settting from server.properties", e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
	
					}
				}
			}
		}
		if(log.isDebugEnabled()){
			log.debug("Local settting was loaded, setting :"+this.currentSetting);
		}
	}
	
	protected String getLocatorServerUrl() {
		return this.currentSetting.get(PREF_KEY_LOCATOR_SERVER);
	}

	private void updateFromRemote() {
		if(log.isDebugEnabled()){
			log.debug("Going to update local settting from server ...");
		}
		try {
		    String digest = this.currentSetting.get(PREF_KEY_DIGEST);
			byte[] data = context.getService(IRestProxyService.class).getRestService(IURLLocatorResource.class,IURLLocatorResource.class,getLocatorServerUrl()).getURLSettings(digest);
			Map<String, String> map = null;
			if (data==null||(map = fromBytes(data))==null) {
				if(log.isInfoEnabled()){
					log.info("Server doesn't return any thing !");
				}
				return;
			}
			if(log.isInfoEnabled()){
				log.info("Server return new setting :"+map);
			}
			IPreferenceManager prefManager = context.getService(IPreferenceManager.class);	
			if(map.size() > 0){
				for (Entry<String,String> entry : map.entrySet()) {
					String key = StringUtils.trimToNull(entry.getKey());
					String val = StringUtils.trimToNull(entry.getValue());
					if((key != null)&&(val != null)){
						prefManager.updatePreference(getModuleName(), key, val);
					}
				}
			}
			digest = new Md5(data).getStringDigest();
			prefManager.updatePreference(getModuleName(), PREF_KEY_DIGEST, digest);
			if(this.latch != null){
				this.latch.countDown();
				this.latch = null;
			}
			if(log.isInfoEnabled()){
				log.info("Local setting was updated from server, new local setting :"+this.currentSetting);
			}

		} catch (Throwable e) {
			log.warn("Error when loading properties from server", e);
		}
	}
	
	@SuppressWarnings({ "unchecked"})
	private Map<String,String> fromBytes(byte[] data) {
		if (data == null) {
			return null;
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bis);
			return (Map<String,String>)ois.readObject();
		} catch (Throwable e) {
			log.warn("Error when data info from bytes", e);
			return null;
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
		if(this.latch != null){		// waiting remote updating finish
			try {
				this.latch.await(3, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
			}
			this.latch = null;
		}
		return this.currentSetting.get(name);
	}

	public String getModuleName() {
		return "URLManagement";
	}

	/**
	 * @return the checkIntervalInSeconds
	 */
	public int getCheckIntervalInSeconds() {
		return checkIntervalInSeconds;
	}

	/**
	 * @param checkIntervalInSeconds the checkIntervalInSeconds to set
	 */
	public void setCheckIntervalInSeconds(int checkIntervalInSeconds) {
		this.checkIntervalInSeconds = checkIntervalInSeconds;
	}
	
	

	
}
