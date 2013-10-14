/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.app.Activity;

import com.wxxr.mobile.core.ui.api.IPage;

/**
 * @author neillin
 *
 */
public interface IBindableActivity {
	void showView(String viewId);
	void hideView(String viewId);
	IPage getBindingPage();
	Activity getActivity();
}
