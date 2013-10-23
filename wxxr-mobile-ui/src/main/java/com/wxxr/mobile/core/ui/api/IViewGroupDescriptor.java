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
	IViewGroupDescriptor setDynamic(boolean bool);
	String[] getViewIds();
	boolean isDynamic();
	IViewGroup createViewGroup(IWorkbenchRTContext ctx);
}
