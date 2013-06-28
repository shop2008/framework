/**
 * 
 */
package com.wxxr.mobile.android.ui;

import com.wxxr.mobile.core.ui.api.IPageNavigator;

/**
 * @author neillin
 *
 */
public interface IAndroidPageNavigator extends IPageNavigator {
	void onPageCreate(String pageId, BindableActivity activity);
	void onPageShow(String pageId);
	void onPageHide(String pageId);
	void onPageDetroy(String pageId);
	BindableActivity getOnShowActivity();
}
