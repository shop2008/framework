/**
 * 
 */
package com.wxxr.mobile.android.ui;

import static com.wxxr.mobile.android.ui.BindingUtils.getBindingDescriptor;
import static com.wxxr.mobile.android.ui.BindingUtils.getNavigator;
import static com.wxxr.mobile.android.ui.BindingUtils.getViewBinder;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IViewBinding;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;

/**
 * @author neillin
 *
 */
public abstract class BindableFragmentActivity extends FragmentActivity implements IBindableActivity {

	private static final Trace log = Trace.register(BindableFragmentActivity.class);
	
	private IViewBinding androidViewBinding;
	private Map<String, BindableFragment> fragments;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		this.androidViewBinding = getViewBinder().createBinding(new IAndroidBindingContext() {
			
			@Override
			public Context getUIContext() {
				return getActivity();
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
	

	public void addFragment(BindableFragment frag){
		if(this.fragments == null){
			this.fragments = new HashMap<String, BindableFragment>();
		}
		this.fragments.put(frag.getViewId(), frag);
	}
	
	public BindableFragment getFragment(String name){
		return this.fragments != null ? fragments.get(name) : null;
	}
	
	public BindableFragment removeFragment(String name){
		return this.fragments != null ? fragments.remove(name) : null;
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IBindableActivity#getViewBinding()
	 */
	@Override
	public IViewBinding getViewBinding() {
		return this.androidViewBinding;
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
	
	protected void onContentViewCreated(Bundle savedInstanceState){
		
	}

	
	protected void onActivityStarted(){
		
	}

	
	protected void onActivityDestroied(){
		
	}

	protected void onActivityStopped() {
		
	}


}
