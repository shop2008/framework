package com.wxxr.mobile.android.preference;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class DictionaryUtils {
	
	public static Dictionary<String, String> clone(Dictionary<String, String> data){
		Hashtable<String, String> t = new Hashtable<String, String>();
		if(data.size() > 0){
			for(Enumeration<String> enu = data.keys();enu.hasMoreElements();){
				String key = enu.nextElement();
				t.put(key, data.get(key));
			}
		}
		return t;
	}

}
