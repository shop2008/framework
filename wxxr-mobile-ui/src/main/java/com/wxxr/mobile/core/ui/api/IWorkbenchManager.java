/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import com.wxxr.javax.validation.Validator;

/**
 * 
 * C : UI binding context
 * @author neillin
 *
 */
public interface IWorkbenchManager {
	IWorkbenchDescriptor getWorkbenchDescriptor();
	void setWorkbenchDescriptor(IWorkbenchDescriptor workbenchDescriptor);
	IWorkbench getWorkbench();
	IPageNavigator getPageNavigator();
	
	IUICommandExecutor getCommandExecutor();
	
	IUIExceptionHandler getExceptionHandler();
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

	
	Validator getValidator();
	
	IValidationErrorHandler getValidationErrorHandler();
	
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
