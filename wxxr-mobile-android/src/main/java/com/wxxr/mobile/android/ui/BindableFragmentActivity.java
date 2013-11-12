/**
 * 
 */
package com.wxxr.mobile.android.ui;

import static com.wxxr.mobile.android.ui.BindingUtils.getBindingDescriptor;
import static com.wxxr.mobile.android.ui.BindingUtils.getNavigator;
import static com.wxxr.mobile.android.ui.BindingUtils.getViewBinder;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.AbstractDocument;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IAppToolbar;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IPageDescriptor;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinding;
import com.wxxr.mobile.core.ui.api.IWorkbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.common.AbstractPageDescriptor;

/**
 * @author neillin
 *
 */
public abstract class BindableFragmentActivity extends FragmentActivity implements IBindableActivity {


	private static final Trace log = Trace.register(BindableFragmentActivity.class);
	
	private IViewBinding androidViewBinding;
	private Map<String, BindableFragment> fragments;
	private IViewBinding toolbarViewBingding;
	private View contentRoot;
	private IView rootView;
	private IPage page;
	private IAppToolbar toolbar;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		if(log.isDebugEnabled()){
			log.debug("creating activity ...");
		}
		this.page = getBindingPage();
		IPageDescriptor descriptor = AppUtils.getService(IWorkbenchManager.class).getPageDescriptor(page.getName());
		if(descriptor.withToolbar()){
			this.toolbarViewBingding = getViewBinder().createBinding(new IAndroidBindingContext() {
				
				@Override
				public Context getUIContext() {
					return BindableFragmentActivity.this;
				}
				
				@Override
				public View getBindingControl() {
					return null;
				}
	
				@Override
				public IWorkbenchManager getWorkbenchManager() {
					return AppUtils.getService(IWorkbenchManager.class);
				}
			}, getBindingDescriptor(IWorkbench.TOOL_BAR_VIEW_ID));
			this.contentRoot = (View)toolbarViewBingding.getUIControl();
			this.rootView = AppUtils.getService(IWorkbenchManager.class).getWorkbench().createNInitializedView(IWorkbench.TOOL_BAR_VIEW_ID);
			if(this.rootView instanceof IAppToolbar){
				this.toolbar = (IAppToolbar)this.rootView;
				page.onToolbarCreated(toolbar);
			}
			if(descriptor.getViewDescription() != null){
				this.toolbar.setTitle(BindingUtils.getMessage(descriptor.getViewDescription()), null);
			}
		}else{
			this.contentRoot = null;
		}
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
		
		if(this.contentRoot != null){
				ViewGroup vg = (ViewGroup)this.contentRoot.findViewById(RUtils.getInstance().getResourceId(RUtils.CATEGORY_NAME_ID, "contents"));
				vg.addView((View)this.androidViewBinding.getUIControl());
				setContentView(this.contentRoot);
		}else{
			setContentView((View)this.androidViewBinding.getUIControl());
		}
		getNavigator().onPageCreate(page, this);
		onContentViewCreated(savedInstanceState);
		super.onCreate(savedInstanceState);
		if(log.isDebugEnabled()){
			log.debug("Activity created !");
		}
	}
	

	@Override
	public IAppToolbar getToolbar() {
		return this.toolbar;
	}


	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected final void onStart() {
		if(log.isDebugEnabled()){
			log.debug("Starting activity ...");
		}
		IPage page = getBindingPage();
		this.androidViewBinding.activate(page);
		if((this.toolbarViewBingding != null)&&(this.rootView != null)){
			this.toolbarViewBingding.activate(this.rootView);
			if(this.toolbar != null){
				page.onToolbarShow();
			}
		}
		super.onStart();
		getNavigator().onPageShow(page);
		onActivityStarted();
		if(log.isDebugEnabled()){
			log.debug("Activity started !");
		}
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected final void onStop() {
		if(log.isDebugEnabled()){
			log.debug("Stopping activity ...");
		}
		super.onStop();
		if(this.toolbarViewBingding != null){
			this.toolbarViewBingding.deactivate();
		}
		if(this.toolbar != null){
			this.page.onToolbarHide();
		}
		this.androidViewBinding.deactivate();
		getNavigator().onPageHide(getBindingPage());
		onActivityStopped();
		if(log.isDebugEnabled()){
			log.debug("Activity stopped !");
		}
	}

	


	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected final void onDestroy() {
		if(log.isDebugEnabled()){
			log.debug("Destroying activity ...");
		}
		if(this.toolbarViewBingding != null){
			this.toolbarViewBingding.destroy();
			if(this.toolbar != null){
				this.page.onToolbarDestroy();
			}
			this.toolbar = null;
			this.toolbarViewBingding = null;
		}
		this.androidViewBinding.destroy();
		super.onDestroy();
		getNavigator().onPageDetroy(getBindingPage());
		onActivityDestroied();
		if(log.isDebugEnabled()){
			log.debug("Activity destroyed !");
		}
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


	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		if(log.isDebugEnabled()){
			log.debug("Pausing activity ...");
		}
		super.onPause();
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		if(log.isDebugEnabled()){
			log.debug("Resuming activity ...");
		}
		if(this.androidViewBinding != null){
			this.androidViewBinding.refresh();
		}
		super.onResume();
	}


}
