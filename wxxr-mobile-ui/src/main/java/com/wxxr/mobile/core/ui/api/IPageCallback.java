/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IPageCallback {
	void onCreate(IPage page);
	void onShow(IPage page);
	void onHide(IPage page);
	void onDestroy(IPage page);
}
