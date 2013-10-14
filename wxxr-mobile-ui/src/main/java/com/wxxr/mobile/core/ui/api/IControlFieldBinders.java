/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * 每个UI 控件对应一组Field Binder, 每个Field Binder对应一个或多个展示模型类型（IUIComponent）
 * @author neillin
 *
 */
public interface IControlFieldBinders {
	
	<M extends IUIComponent> IFieldBinder getFieldBinder(Class<M> pmodelClass);
	
	<M extends IUIComponent> void registerFieldBinder(Class<M> pmodelClass,IFieldBinder binder);
	
	<M extends IUIComponent> boolean unregisterFieldBinder(Class<M> pmodelClass,IFieldBinder binder);

}
