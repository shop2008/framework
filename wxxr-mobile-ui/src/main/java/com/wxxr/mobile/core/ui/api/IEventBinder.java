/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.Map;



/**
 * 这个接口的实现类负责创建绑定对象对指定控件的特定事件进行监控，当条件满足时，生成InputEvent,并调用绑定的命令(Command)
 * 一种事件对应一个Binder
 * @author neillin
 *
 */
public interface IEventBinder {
	IEventBinding createBinding(IBindingContext context, String fieldName,String cmdName, Map<String, String> attrs);
	void init(IWorkbenchRTContext mngCtx);
	void destory();
}
