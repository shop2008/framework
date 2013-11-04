/**
 * 
 */
package com.wxxr.mobile.core.ui.api;


/**
 * @author neillin
 *
 */
public interface IBinding<M extends IUIComponent> 
{
	void notifyDataChanged(ValueChangedEvent... events);
	void activate(M model);
	void deactivate();
	void destroy();
	void init(IWorkbenchRTContext ctx);
//	IUIComponent getValueModel();
	Object getUIControl();
}
