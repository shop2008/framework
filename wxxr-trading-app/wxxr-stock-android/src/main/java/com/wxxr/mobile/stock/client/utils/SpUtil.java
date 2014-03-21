package com.wxxr.mobile.stock.client.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SpUtil {

	private static Context mContext;
	
	
	private static SharedPreferences sp;
	private static Editor editor;
	private static Object lock = new Object();
	private static SpUtil instance = null;
	private SpUtil() {
		if (mContext != null) {
			sp = mContext.getSharedPreferences(Constants.KEY_APP_CONFIG, Context.MODE_PRIVATE);
			editor = sp.edit();
		}
	}
	
	public static SpUtil getInstance(Context context) {
		if (instance == null) {
			mContext = context;
			instance = new SpUtil();
		}
		return instance;
	}
	
	public void save(String key, String value) {
		synchronized (lock) {
			editor.putString(key, value);
			editor.commit();
		}
	}
	
	public void delete(String key) {
		synchronized (lock) {
			editor.remove(key);
			editor.commit();
		}
	}
	
	public String find(String key) {
		return sp.getString(key, "");
	}
}
