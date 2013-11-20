package com.wxxr.mobile.core.rpc.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.mobile.core.util.StringUtils;

public class MarshallerClassRegistry {
	private static Map<String,Class<?>>  types = new ConcurrentHashMap<String,Class<?>>();
	
	public static void register(Class<?> clazz){
		if (clazz==null) {
			return ;
		}
		String key = null;
		if (clazz.isAnnotationPresent(XmlRootElement.class)) {
			XmlRootElement root = clazz.getAnnotation(XmlRootElement.class);
			if (root!=null) {
				key = root.name();
			}
		}
		if (StringUtils.isBlank(key)) {
			key = clazz.getCanonicalName();
		}
		types.put(key, clazz);
	}
	
	public static boolean unregister(Class<?> clazz){
		if (clazz==null) {
			return false;
		}
		String key = null;
		if (clazz.isAnnotationPresent(XmlRootElement.class)) {
			XmlRootElement root = clazz.getAnnotation(XmlRootElement.class);
			if (root!=null) {
				key = root.name();
			}
		}
		if (StringUtils.isBlank(key)) {
			key = clazz.getCanonicalName();
		}
		Class<?> _clazz = types.remove(key);
		return clazz.equals(_clazz);
	}
	
	public static Class<?> getType(String str){
		return types.get(str);
	}
	

}
