/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IViewGroupDescriptor {
	String getId();
	IViewGroupDescriptor addView(String viewId);
	IViewGroupDescriptor removeView(String viewId);
	String[] getViewIds();
	IViewGroup createViewGroup(IWorkbenchRTContext ctx);
}
