/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.app.Activity;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;

/**
 * @author neillin
 *
 */
public class MainActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		showHomePage();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		showHomePage();
	}

	protected void showHomePage(){
		AppUtils.getService(IWorkbenchManager.class).getWorkbench().showHomePage();
	}
}
