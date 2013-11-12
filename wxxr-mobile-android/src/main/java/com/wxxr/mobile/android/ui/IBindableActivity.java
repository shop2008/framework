/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.app.Activity;

import com.wxxr.mobile.core.ui.api.IDialog;
import com.wxxr.mobile.core.ui.api.IAppToolbar;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinding;

/**
 * @author neillin
 *
 */
public interface IBindableActivity {
	IPage getBindingPage();
	Activity getActivity();
	IViewBinding getViewBinding();
	IDialog createDialog(IView view);
	IAppToolbar getToolbar();
}
