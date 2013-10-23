/**
 * 
 */
package com.wxxr.mobile.android.ui;

import static com.wxxr.mobile.android.ui.BindingUtils.getBindingDescriptor;
import static com.wxxr.mobile.android.ui.BindingUtils.getNavigator;
import static com.wxxr.mobile.android.ui.BindingUtils.getViewBinder;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinding;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

/**
 * @author neillin
 *
 */
public abstract class BindableActivity extends Activity implements IBindableActivity{

	private static final Trace log = Trace.register(BindableActivity.class);
	
	private IViewBinding androidViewBinding;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		this.androidViewBinding = getViewBinder().createBinding(new IAndroidBindingContext() {
			
			@Override
			public Context getUIContext() {
				return BindableActivity.this;
			}
			
			@Override
			public View getBindingControl() {
				return null;
			}

			@Override
			public IWorkbenchManager getWorkbenchManager() {
				return AppUtils.getService(IWorkbenchManager.class);
			}
		}, getBindingDescriptor(getBindingPageId()));
		setContentView((View)this.androidViewBinding.getUIControl());
		super.onCreate(savedInstanceState);
		getNavigator().onPageCreate(getBindingPage(), this);
		onContentViewCreated(savedInstanceState);
	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected final void onStart() {
		this.androidViewBinding.activate(getBindingPage());
		super.onStart();
		getNavigator().onPageShow(getBindingPage());
		onActivityStarted();
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected final void onStop() {
		super.onStop();
		this.androidViewBinding.deactivate();
		getNavigator().onPageHide(getBindingPage());
		onActivityStopped();
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected final void onDestroy() {
		this.androidViewBinding.destroy();
		super.onDestroy();
		getNavigator().onPageDetroy(getBindingPage());
		onActivityDestroied();
	}
	
		
	protected abstract String getBindingPageId();

	@Override
	public IPage getBindingPage() {
		return AppUtils.getService(IWorkbenchManager.class).getWorkbench().getPage(getBindingPageId());
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IBindableActivity#getActivity()
	 */
	@Override
	public Activity getActivity() {
		return this;
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IBindableActivity#getViewBinding()
	 */
	@Override
	public IViewBinding getViewBinding() {
		return this.androidViewBinding;
	}
	
	protected void onContentViewCreated(Bundle savedInstanceState){
		
	}

	
	protected void onActivityStarted(){
		
	}

	
	protected void onActivityDestroied(){
		
	}

	protected void onActivityStopped() {
		
	}

}
