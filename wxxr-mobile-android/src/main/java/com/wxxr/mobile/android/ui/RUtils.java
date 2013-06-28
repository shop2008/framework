/**
 * 
 */
package com.wxxr.mobile.android.ui;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author neillin
 *
 */
public class RUtils {
	
	private static HashMap<String, RUtils> map = new HashMap<String, RUtils>();
	
	public static RUtils getInstance(String packageName){
		synchronized(map){
			RUtils inst = map.get(packageName);
			if(inst == null){
				inst = new RUtils(packageName);
				map.put(packageName, inst);
			}
			return inst;
		}
	}
	
	private final String packageName;
	private Map<String, Integer> idsMap,layoutsMap;
	
	private RUtils(String packageName){
		this.packageName = packageName;
	}
	
	public synchronized final Integer getIdResourceId(String name){
		if(this.idsMap == null){
			initIdsMap();
		}
		return this.idsMap.get(name);
	}

	public synchronized final Integer getLayoutResourceId(String name){
		if(this.layoutsMap == null){
			initLayoutsMap();
		}
		return this.layoutsMap.get(name);
	}
	
	protected void initIdsMap() {
		try {
			Map<String, Integer> map = new HashMap<String, Integer>();
			Class<?> clazz = Class.forName(this.packageName+".R$id");
			Field[] fields = clazz.getFields();
			if(fields != null){
				for (Field field : fields) {
					if(Modifier.isStatic(field.getModifiers())&&(field.isSynthetic() == false)){
						String fname = field.getName();
						try {
							Integer val = field.getInt(null);
							map.put(fname, val);
						} catch (Throwable e) {
						}
					}
				}
			}
			this.idsMap = map;
		}catch(Throwable t){
			throw new RuntimeException("Failed to introspect R resource class", t);
		}
	}
	
	protected void initLayoutsMap() {
		try {
			Map<String, Integer> map = new HashMap<String, Integer>();
			Class<?> clazz = Class.forName(this.packageName+".R$layout");
			Field[] fields = clazz.getFields();
			if(fields != null){
				for (Field field : fields) {
					if(Modifier.isStatic(field.getModifiers())&&(field.isSynthetic() == false)){
						String fname = field.getName();
						try {
							Integer val = field.getInt(null);
							map.put(fname, val);
						} catch (Throwable e) {
						}
					}
				}
			}
			this.idsMap = map;
		}catch(Throwable t){
			throw new RuntimeException("Failed to introspect R resource class", t);
		}
	}

}
