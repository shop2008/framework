/**
 * 
 */
package com.wxxr.mobile.core.ui.utils;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.core.ui.api.IValueConvertor;

/**
 * @author neillin
 *
 */
public class ConvertorRegistry {
	
	private static ConvertorRegistry instance;
	
	public synchronized static final ConvertorRegistry getInstance() {
		if(instance == null){
			 instance = new ConvertorRegistry();
			 instance.init();
		}
		return instance;
	}
		
	private Map<Class<?>, IValueConvertor<?>> converters = new HashMap<Class<?>, IValueConvertor<?>>();
	
	private ConvertorRegistry(){}

	public <T> void registerConverter(Class<T> clazz, IValueConvertor<T> converter){
		if((clazz == null)||(converter == null)){
			throw new IllegalArgumentException("Invalid converter, neither clazz nor converter could be null !!!");
		}
		this.converters.put(clazz, converter);
	}
	
	
	public <T> boolean unregisterConverter(Class<T> clazz, IValueConvertor<T> converter){
		if((clazz == null)||(converter == null)){
			return false;
		}
		IValueConvertor<?> old = this.converters.get(clazz);
		if(old == converter){
			this.converters.remove(clazz);
			return true;
		}
		return false;
	}

	
	@SuppressWarnings("unchecked")
	public <T> IValueConvertor<T> getConverter(Class<T> clazz){
		return (IValueConvertor<T>)this.converters.get(clazz);
	}
	
	private void init(){
		registerConverter(Boolean.class, new BooleanValueConvertor());
		registerConverter(String.class, new StringValueConvertor());
		registerConverter(Integer.class, new IntegerValueConvertor());
		registerConverter(Long.class, new LongValueConvertor());
	}
}
