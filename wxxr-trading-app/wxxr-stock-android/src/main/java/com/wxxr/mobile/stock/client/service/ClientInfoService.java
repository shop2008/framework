package com.wxxr.mobile.stock.client.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

import com.wxxr.mobile.android.preference.DictionaryUtils;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.preference.api.IPreferenceManager;
import com.wxxr.mobile.stock.app.IStockAppContext;

public class ClientInfoService extends AbstractModule<IStockAppContext> implements IClientInfoService {

	private static final String KEY_ALERT_ENABLED = "AlertEnabled";
	private static final String MODULE_NAME = "ClientInfoConfig";
	
	private static final String KEY_GUIDE_GAINED = "GuideGained";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private static final String KEY_ALERT_SHOWN = "alertDialogShown";
	@Override
	protected void initServiceDependency() {
		addRequiredService(IWorkbenchManager.class);
	}

	@Override
	protected void startService() {
		context.registerService(IClientInfoService.class, this);
		loadConfig();
	}

	private void loadConfig() {
		updateDialogShowStatus(false);
		setAlertUpdateEnabled(true);
	}

	@Override
	protected void stopService() {
		context.unregisterService(IClientInfoService.class, this);
	}

	@Override
	public void setAlertUpdateEnabled(boolean isEnabled) {
		IPreferenceManager mgr = getPrefManager();
		Dictionary<String, String> dictionary = mgr.getPreference(getModuleName());
		
		if(dictionary == null){
			dictionary= new Hashtable<String, String>();
            getPrefManager().newPreference(getModuleName(), dictionary);
        }else{
        	dictionary = DictionaryUtils.clone(dictionary);
        }
	
		dictionary.put(KEY_ALERT_ENABLED, String.valueOf(isEnabled)+"_"+sdf.format(new Date()));
		mgr.putPreference(getModuleName(), dictionary);
	}

	@Override
	public boolean alertUpdateEnabled() {
		Dictionary<String, String> dictionary = getPrefManager().getPreference(getModuleName());
		if(dictionary != null) {
			//return Boolean.parseBoolean(dictionary.get(KEY_ALERT_ENABLED));
			String alertEnableStr = dictionary.get(KEY_ALERT_ENABLED);
			if (StringUtils.isNotBlank(alertEnableStr)) {
				
				if(!("false_"+sdf.format(new Date())).equals(alertEnableStr)) {
					return true;
				}
				if (alertEnableStr.contains("true")) {
					return true;
				} else if (alertEnableStr.contains("false")) {
					return false;
				}
			} else {
				return true;
			}
		}
		return true;
	}
	
	
	@Override
	public String getModuleName() {
		return MODULE_NAME;
	}
	private IPreferenceManager getPrefManager() {
		return this.context.getService(IPreferenceManager.class);
	}

	@Override
	public void setGuideGainned(boolean gainnd, String phoneNum) {
		// TODO Auto-generated method stub
		IPreferenceManager mgr = getPrefManager();
		Dictionary<String, String> dictionary = mgr.getPreference(getModuleName());
	
		if(dictionary == null){
			dictionary= new Hashtable<String, String>();
            getPrefManager().newPreference(getModuleName(), dictionary);
        }else{
        	dictionary = DictionaryUtils.clone(dictionary);
        }
		
		dictionary.put(KEY_GUIDE_GAINED+"_"+phoneNum, String.valueOf(gainnd));
		mgr.putPreference(getModuleName(), dictionary);
	}

	@Override
	public boolean getGuideGainnedByUserId(String phoneNum) {
		Dictionary<String, String> dictionary = getPrefManager().getPreference(getModuleName());
		if(dictionary != null) {
			String ss = dictionary.get(KEY_GUIDE_GAINED+"_"+phoneNum);
			if(StringUtils.isNotBlank(ss)) {
				return Boolean.parseBoolean(ss);
			} 
		}
		return false;
	}

	@Override
	public boolean alertUpdateDialogShown() {
		Dictionary<String, String> dictionary = getPrefManager().getPreference(getModuleName());
		if(dictionary != null) {
			String alertEnableStr = dictionary.get(KEY_ALERT_SHOWN);
			if(alertEnableStr.equals("false") || alertEnableStr.equals("true")) {
				return Boolean.valueOf(alertEnableStr);
			}
		}
		return false;
	}

	@Override
	public void updateDialogShowStatus(boolean isShown) {
		IPreferenceManager mgr = getPrefManager();
		Dictionary<String, String> dictionary = mgr.getPreference(getModuleName());
		if(dictionary == null){
			dictionary= new Hashtable<String, String>();
            getPrefManager().newPreference(getModuleName(), dictionary);
        }else{
        	dictionary = DictionaryUtils.clone(dictionary);
        }
		dictionary.put(KEY_ALERT_SHOWN, String.valueOf(isShown));
		mgr.putPreference(getModuleName(), dictionary);
	}

}
