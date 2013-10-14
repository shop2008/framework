/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.Map;



/**
 * 这个接口的实现类负责将view ，page中各个字段绑定到对应界面中具体的控件上
 * C : context type, T : control type
 * @author neillin
 *
 */
public interface IFieldBinder {
	IBinding<IUIComponent> createBinding(IBindingContext context, String fieldName,Map<String, String> attrs);
	void init(IWorkbenchRTContext mngCtx);
	void destory();
}
