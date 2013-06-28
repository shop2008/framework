/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IWorkbenchManager {
	IWorkbench getWorkbench();
	IView createView(String viewName);
}
