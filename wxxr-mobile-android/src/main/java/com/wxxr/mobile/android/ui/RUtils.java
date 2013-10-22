/**
 * 
 */
package com.wxxr.mobile.android.ui;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.log.api.Trace;

/**
 * @author neillin
 *
 */
public class RUtils {
	public static final String RESOURCEID_PROTOCOL = "resourceId";
	
	public static final String CATEGORY_NAME_ID ="id";
	public static final String CATEGORY_NAME_LAYOUT ="layout";
	public static final String CATEGORY_NAME_ATTR ="attr";
	public static final String CATEGORY_NAME_ARRAY ="array";
	public static final String CATEGORY_NAME_ANIMATION ="anim";
	public static final String CATEGORY_NAME_COLOR ="color";
	public static final String CATEGORY_NAME_DIMEN ="dimen";
	public static final String CATEGORY_NAME_DRAWABLE ="drawable";
	public static final String CATEGORY_NAME_MENU ="menu";
	public static final String CATEGORY_NAME_STRING ="string";
	public static final String CATEGORY_NAME_STYLE ="style";
	public static final String CATEGORY_NAME_STYLEABLE ="styleable";
	
	private static final Trace log = Trace.register(RUtils.class);
	
	private static HashMap<String, RUtils> map = new HashMap<String, RUtils>();
	
	public static RUtils getInstance() {
		return getInstance(AppUtils.getFramework().getApplicationId());
	}
	
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
	private Map<String, Map<String, Integer>> valueMaps = new HashMap<String, Map<String,Integer>>();
	
	private RUtils(String packageName){
		this.packageName = packageName;
	}
	
	public static boolean isResourceIdURI(String uri){
		return uri.startsWith(RESOURCEID_PROTOCOL+":");
	}
	
	public synchronized final int getResourceIdByURI(String resourceURI){
		if(!isResourceIdURI(resourceURI)){
			throw new InvalidResourceIdURIException(resourceURI);
		}
		resourceURI = resourceURI.substring(11);
		String category = CATEGORY_NAME_ID;
		int idx = resourceURI.indexOf('/');
		if(idx > 0){
			category = resourceURI.substring(0,idx);
			resourceURI = resourceURI.substring(idx+1);
		}
		return getResourceId(category, resourceURI);
	}
	
	public synchronized final int getResourceId(String category,String name){
		Map<String,Integer> map = this.valueMaps.get(category);
		if(map == null){
			map = initValueMap(category);
		}
		Integer val = map.get(name);
		if(val == null){
			throw new InvalidResourceIdException(category+"/"+name);
		}
		return val.intValue();
	}
	
	protected Map<String,Integer> initValueMap(String category) {
		try {
			Map<String, Integer> map = new HashMap<String, Integer>();
			Class<?> clazz = Class.forName(this.packageName+".R$"+category);
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
			this.valueMaps.put(category, map);
			return map;
		}catch(Throwable t){
			log.error("Failed to introspect R resource class for category :"+category, t);
			throw new InvalidResourceIdException("Invalid category :"+category);
		}
	}
	
}
