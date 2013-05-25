/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IWorkbench {
	String[] getPageIds();
	String getActivePageId();
	void addPage(IPage page);
	IPage removePage(String pageId);
	IPage getPage(String pageId);
	void showPage(String pageId);
}
