/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IPageDescriptor extends IViewDescriptor {
	IViewGroupDescriptor createViewGroup(String name);
	IViewGroupDescriptor removeViewGroup(String name);
	String[] getAllViewGroupNames();
	IViewGroupDescriptor getViewGroup(String name);
}
