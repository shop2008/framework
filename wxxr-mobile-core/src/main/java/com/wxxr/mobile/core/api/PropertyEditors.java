package com.wxxr.mobile.core.api;

import java.util.concurrent.ConcurrentHashMap;


public class PropertyEditors {
	private static ConcurrentHashMap<Class<?>, PropertyEditor<?>> map = new ConcurrentHashMap<Class<?>, PropertyEditor<?>>();
	
	
	public static <T> void registerEditor(Class<T> clazz, PropertyEditor<T> editor){
		map.put(clazz, editor);
	}
	
	
	@SuppressWarnings("rawtypes")
	public static <T> boolean unregisterEditor(Class<T> clazz, PropertyEditor<T> editor){
		PropertyEditor old = map.get(clazz);
		if(old == editor){
			map.remove(clazz);
			return true;
		}
		return false;
	}
	
	public static Object toValue(Class<?> type, String text){
		return null;
	}

	
	public static String toString(Class<?> type, Object text){
		return null;
	}
}
