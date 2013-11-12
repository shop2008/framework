/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.app.Activity;

import com.wxxr.mobile.core.ui.api.IAppToolbar;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IViewBinding;

/**
 * @author neillin
 *
 */
public interface IBindableActivity {
	IPage getBindingPage();
	Activity getActivity();
	IViewBinding getViewBinding();
	IAppToolbar getToolbar();
}
