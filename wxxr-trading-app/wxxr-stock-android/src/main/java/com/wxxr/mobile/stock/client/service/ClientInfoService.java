package com.wxxr.mobile.stock.client.service;

import java.util.Dictionary;
import java.util.Hashtable;

import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.preference.api.IPreferenceManager;
import com.wxxr.mobile.stock.app.IStockAppContext;

public class ClientInfoService extends AbstractModule<IStockAppContext> implements IClientInfoService {

	private static final String KEY_ALERT_ENABLED = "AlertEnabled";
	private static final String MODULE_NAME = "ClientInfo";
	@Override
	protected void initServiceDependency() {
		addRequiredService(IWorkbenchManager.class);
	}

	@Override
	protected void startService() {
		context.registerService(IClientInfoService.class, this);

	}

	@Override
	protected void stopService() {
		context.unregisterService(IClientInfoService.class, this);
	}

	@Override
	public void setAlertUpdateEnabled(boolean isEnabled) {
		IPreferenceManager mgr = getPrefManager();
		Dictionary<String, String> dictionary = mgr.getPreference(MODULE_NAME);
		if(dictionary != null) {
			dictionary.put(KEY_ALERT_ENABLED, String.valueOf(isEnabled));
		} else {
			dictionary = new Hashtable<String, String>();
			dictionary.put(KEY_ALERT_ENABLED, String.valueOf(isEnabled));
			mgr.newPreference(MODULE_NAME, dictionary);
		}
		mgr.putPreference(getModuleName(), dictionary);
	}

	@Override
	public boolean alertUpdateEnabled() {
		Dictionary<String, String> dictionary = getPrefManager().getPreference(MODULE_NAME);
		if(dictionary != null) {
			return Boolean.parseBoolean(dictionary.get(KEY_ALERT_ENABLED));
		}
		return true;
	}
	
	private IPreferenceManager getPrefManager() {
		return this.context.getService(IPreferenceManager.class);
	}

}
