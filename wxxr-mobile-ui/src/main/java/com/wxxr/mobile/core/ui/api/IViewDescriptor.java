/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IViewDescriptor {
	String getViewId();
	String getViewName();
	String getViewDescription();
	ViewType getViewType();
	boolean isSingleton();
	IBindingDescriptor getBindingDescriptor(TargetUISystem targetSystem);
	IView createPresentationModel(IWorkbenchRTContext ctx);
}
