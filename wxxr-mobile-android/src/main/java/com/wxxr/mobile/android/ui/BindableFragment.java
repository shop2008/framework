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
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IAppToolbar;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.ISelectionProvider;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public abstract class BindableFragment extends Fragment {
	
	private IBinding<IView> androidViewBinding;
	private BindableFragmentActivity fragActivity;
	private boolean onShow;
	private ISelectionProvider provider;
	private IView bindingView;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if(getLogger().isDebugEnabled()){
			getLogger().debug("onActivityCreated ...");
		}

		super.onActivityCreated(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		if(getLogger().isDebugEnabled()){
			getLogger().debug("onAttach ...");
		}
		super.onAttach(activity);
		if(activity instanceof BindableFragmentActivity){
			this.fragActivity = (BindableFragmentActivity)activity;
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if(getLogger().isDebugEnabled()){
			getLogger().debug("onCreate ...");
		}
		this.bindingView = getBindingView();
		super.onCreate(savedInstanceState);
		getNavigator().onViewCreate(bindingView, this.fragActivity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(getLogger().isDebugEnabled()){
			getLogger().debug("onCreateView ...");
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

			@Override
			public boolean isOnShow() {
				return onShow;
			}

			@Override
			public void hideView() {
				((IBindableActivity)getActivity()).hideFragment(getViewId());
			}
		}, getBindingDescriptor(getViewId()));
		
		if(bindingView instanceof ViewBase){
			((ViewBase)bindingView).onUICreate();
		}		
		return (View)this.androidViewBinding.getUIControl();
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		if(getLogger().isDebugEnabled()){
			getLogger().debug("onDestroy ...");
		}
		if(this.provider != null){
			AppUtils.getService(IWorkbenchManager.class).getWorkbench().getSelectionService().unregisterProvider(this.provider);
			this.provider = null;
		}
		this.androidViewBinding.destroy();
		super.onDestroy();
		getNavigator().onViewDetroy(bindingView);
		this.bindingView = null;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		if(getLogger().isDebugEnabled()){
			getLogger().debug("onDestroyView ...");
		}
		if(bindingView instanceof ViewBase){
			((ViewBase)bindingView).onUIDestroy();
		}
		super.onDestroyView();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDetach()
	 */
	@Override
	public void onDetach() {
		if(getLogger().isDebugEnabled()){
			getLogger().debug("onDetach ...");
		}
		super.onDetach();
		if(this.fragActivity != null){
			this.fragActivity.removeFragment(getViewId());
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onInflate(android.app.Activity, android.util.AttributeSet, android.os.Bundle)
	 */
	@Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
		if(getLogger().isDebugEnabled()){
			getLogger().debug("onInflate ...");
		}

		super.onInflate(activity, attrs, savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		if(getLogger().isDebugEnabled()){
			getLogger().debug("onPause ...");
		}

		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		if(getLogger().isDebugEnabled()){
			getLogger().debug("onResume ...");
		}
		if(this.androidViewBinding != null){
			this.androidViewBinding.doUpdate();
		}
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		if(getLogger().isDebugEnabled()){
			getLogger().debug("onSaveInstanceState ...");
		}

		super.onSaveInstanceState(outState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		if(getLogger().isDebugEnabled()){
			getLogger().debug("onStart ...");
		}
		IAppToolbar toolbar = ((IBindableActivity)getActivity()).getToolbar();
		if(toolbar != null){
			toolbar.attach(this.bindingView);
			IViewDescriptor descriptor = AppUtils.getService(IWorkbenchManager.class).getViewDescriptor(getBindingView().getName());
			String desc = descriptor.getViewDescription();
			if(StringUtils.isNotBlank(desc)){
				toolbar.setTitle(BindingUtils.getMessage(descriptor.getViewDescription()), null);
			}else{
				toolbar.setTitle("",null);
			}
		}
		this.androidViewBinding.activate(this.bindingView);
		super.onStart();
		this.provider = this.bindingView.getSelectionProvider();
		if(this.provider != null){
			AppUtils.getService(IWorkbenchManager.class).getWorkbench().getSelectionService().registerProvider(this.provider);
		}
		this.onShow = true;
		getNavigator().onViewShow(bindingView);		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		if(getLogger().isDebugEnabled()){
			getLogger().debug("onStop ...");
		}
		this.onShow = false;
		IAppToolbar toolbar = ((IBindableActivity)getActivity()).getToolbar();
		if(toolbar != null){
			toolbar.dettach(getBindingView());
		}
		this.androidViewBinding.deactivate();
		super.onStop();
		getNavigator().onViewHide(bindingView);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		if(getLogger().isDebugEnabled()){
			getLogger().debug("onViewCreated ...");
		}

		super.onViewCreated(view, savedInstanceState);
	}

	
	public boolean onBackPressed() {
		return false;
	}
	
	protected IView getBindingView() {
		return ((IBindableActivity)getActivity()).getBindingPage().getView(getViewId());
	}
	
	protected abstract String getViewId();
	
	protected abstract Trace getLogger();

}
