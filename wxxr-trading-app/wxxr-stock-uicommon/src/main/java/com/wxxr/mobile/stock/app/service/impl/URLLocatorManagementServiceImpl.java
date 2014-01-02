/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Base64;

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
	private static final Trace log = Trace.register("com.wxxr.mobile.stock.app.service.impl.URLLocatorManagementServiceImpl");
	private long lastCheckTime;
	private int checkIntervalInSeconds = 30*60;		// 30 minutes
	private String serverUrl;
	private final static String  RELATIVE_URI_APPLICATION ="/mobilestock2";
	private final static String  RELATIVE_URI_MAGNOLIA ="/magnoliaPublic";
	private Dictionary<String, String> defaultSettings = new Hashtable<String, String>();
	private Timer timer;
	Map<String,String> remoteConfig;
	@Override
	public String getServerURL() {
		return serverUrl+RELATIVE_URI_APPLICATION;
	}

	@Override
	public String getMagnoliaURL() {
		return serverUrl+RELATIVE_URI_MAGNOLIA;
	}

	@Override
	protected void initServiceDependency() {
		addRequiredService(IPreferenceManager.class);
	}

	@Override
	protected void startService() {
		if (log.isDebugEnabled()) {
			log.debug("Loading default settings...");
		}
		loadDefaultSettings();//加载出厂设置
		this.serverUrl = getURL("server");
		getService(IRestProxyService.class).setDefautTarget(getServerURL());
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
		}, 1000,60000);
		context.registerService(IURLLocatorManagementService.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(IURLLocatorManagementService.class,this);
	}

	private void loadDefaultSettings() {
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
		    byte[] localData = toBytes(remoteConfig);
		    String digest =  null;
		    if (localData!=null) {
		       digest = Base64.encodeToString(localData, Base64.NO_WRAP);
            }
			byte[] data = context.getService(IRestProxyService.class).getRestService(IURLLocatorResource.class).getURLSettings(digest);
			if (data==null||(remoteConfig = fromBytes(data))==null) {
				return;
			}
			IPreferenceManager prefManager = context.getService(IPreferenceManager.class);	
			String prefName = getModuleName();
			if (!prefManager.hasPreference(prefName)) {
				Dictionary<String, String> d = new Hashtable<String, String>();
				prefManager.newPreference(prefName, d);
			}
			String sUrl =  remoteConfig.get("server");
			if (StringUtils.isNotBlank(sUrl)) {
				this.serverUrl = sUrl;
				if (isChanged(getModuleName(), "server", sUrl)) {
				    getService(IRestProxyService.class).setDefautTarget(getServerURL());
					prefManager.updatePreference(prefName, "server", sUrl);
				}
			}
		} catch (Exception e) {
			log.warn("Error when loading properties from server", e);
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
	private byte[] toBytes(Map<String,String> data) throws Exception {
       if (data == null) {
           return null;
       }
       ByteArrayOutputStream bos = new ByteArrayOutputStream();
       ObjectOutputStream oos = null;
       try {
           oos = new ObjectOutputStream(bos);
           oos.writeObject(data);
           return bos.toByteArray();
       } catch (Exception e) {
           log.warn("Error when data info from bytes", e);
           throw e;
       } finally {
           try {
               if (oos != null) {
                   oos.close();
               }
               bos.close();
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
