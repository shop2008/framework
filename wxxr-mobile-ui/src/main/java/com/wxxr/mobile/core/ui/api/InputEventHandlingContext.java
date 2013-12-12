/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface InputEventHandlingContext {
	Object getUIControl();
	IWorkbenchRTContext getWorkbenchContext();
	String getCommandName();
	String getFieldName();
	IView getViewModel();
}
