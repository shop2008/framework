/**
 * 
 */
package com.wxxr.mobile.core.ui.api;



/**
 * 管理
 * @author neillin
 *
 */
public interface IEventBinderManager {
	/**
	 * return binding strategy for given android UI component
	 * @param clazz
	 * @return
	 */
	IEventBinder getFieldBinder(String eventType);
	
	void registerFieldBinder(String eventType,IEventBinder binder);
	
	boolean unregisterFieldBinder(String eventType,IEventBinder binder);
}
