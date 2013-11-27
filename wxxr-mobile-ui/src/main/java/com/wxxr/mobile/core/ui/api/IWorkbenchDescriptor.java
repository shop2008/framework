/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IWorkbenchDescriptor {
	String getTitle();
	String getDescription();
	INavigationDescriptor[] getExceptionNavigations();
	IWorkbench createWorkbench(IWorkbenchRTContext ctx);
}
