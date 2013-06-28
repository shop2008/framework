/**
 * 
 */
package com.wxxr.mobile.android.ui;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IUIBinder;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * @author neillin
 *
 */
public abstract class BindableActivity extends Activity {

	private static final Trace log = Trace.register(BindableActivity.class);
	
	private IBinding binding;
	private View layoutView;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.layoutView = setupContentView();
		super.onCreate(savedInstanceState);
		IUIBinder<BindableActivity> binder = AppUtils.getService(IWorkbenchManager.class).getUIBinder(BindableActivity.class);
		this.binding = binder.doBinding(this);
		AppUtils.getService(IAndroidPageNavigator.class).onPageCreate(((UIBinding)this.binding).getPage().getName(), this);
	}
	
	
	protected View setupContentView() {
		setContentView(getActivityLayoutId());
		return findViewById(android.R.id.content);
	}

	protected abstract int getActivityLayoutId();

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		if(this.binding != null){
			this.binding.activate();
		}
		super.onStart();
		AppUtils.getService(IAndroidPageNavigator.class).onPageShow(((UIBinding)this.binding).getPage().getName());

	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		if(this.binding != null){
			this.binding.deactivate();
		}
		AppUtils.getService(IAndroidPageNavigator.class).onPageHide(((UIBinding)this.binding).getPage().getName());
	}



	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if(this.binding != null){
			this.binding.destroy();
			this.binding = null;
		}
		super.onDestroy();
		AppUtils.getService(IAndroidPageNavigator.class).onPageDetroy(((UIBinding)this.binding).getPage().getName());
	}
	
	public View getLayoutView() {
		return this.layoutView;
	}

}
