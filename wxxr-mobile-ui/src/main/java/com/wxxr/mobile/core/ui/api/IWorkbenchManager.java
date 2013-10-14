/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * 
 * C : UI binding context
 * @author neillin
 *
 */
public interface IWorkbenchManager {
	IWorkbench getWorkbench();
	IPageNavigator getPageNavigator();
	IUICommandExecutor getCommandExecutor();
	
	/**
	 * return Field binder manager which is suitable for specific target UI context
	 * @param contextType
	 * @return
	 */
	IFieldBinderManager getFieldBinderManager();
	
	/**
	 * return event binder manager which is suitable for specific target UI context
	 * @param contextType
	 * @return
	 */
	IEventBinderManager getEventBinderManager();

	
	/**
	 * return View binder which is suitable for specific target UI context
	 * @param contextType
	 * @return
	 */
	IViewBinder getViewBinder();
	
	IFieldAttributeManager getFieldAttributeManager();
	IPageDescriptor getPageDescriptor(String pageId);
	IViewDescriptor getViewDescriptor(String viewId);
	String[] getAllRegisteredPageIds();
	String[] getAllRegisteredViewIds();
	IWorkbenchManager registerView(IViewDescriptor descriptor);
}
