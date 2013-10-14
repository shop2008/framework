/**
 * 
 */
package com.wxxr.mobile.core.ui.api;



/**
 * 管理界面控件对应的Field Binder
 * C 为界面控件运行的环境（上下文）
 * @author neillin
 *
 */
public interface IFieldBinderManager {
	/**
	 * return binding strategy for given android UI component
	 * @param clazz
	 * @return
	 */
	<M extends IUIComponent> IFieldBinder getFieldBinder(Class<M> pmodelClass,Class<?> controlClass);
	
	<M extends IUIComponent> void registerFieldBinder(Class<M> pmodelClass,Class<?> controlClass,IFieldBinder binder);
	
	<M extends IUIComponent> boolean unregisterFieldBinder(Class<M> pmodelClass,Class<?> controlClass,IFieldBinder binder);
}
