/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IUIUpdatingContext {
	IBindingContext getBindingContext();
	IWorkbenchRTContext getWorkbenchContext();
	IView getView();
}
