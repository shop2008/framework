package com.wxxr.mobile.android.ui;

import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.core.api.ApplicationFactory;
import com.wxxr.mobile.core.api.IApplication;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IUIBinder;
import com.wxxr.mobile.core.ui.api.IWorkbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;

public class AndroidUIBinder<C extends IAndroidAppContext> extends AbstractModule<C> implements IUIBinder<BindableActivity> {

	@Override
	public IBinding doBinding(final BindableActivity target) {
		IApplication<?,?> app = ApplicationFactory.getInstance().getApplication();
		IWorkbench workbench = app.getService(IWorkbenchManager.class).getWorkbench();
		String pageName = target.getClass().getSimpleName();
		if(pageName.endsWith("Activity")){
			pageName = pageName.substring(0,pageName.length()-8);
		}
		String pName = "p_"+pageName;
		final IPage page = workbench.getPage(pName);
		if(page != null){
			UIBinding binding = new UIBinding(page, target);
			binding.init(new IBindingContext() {

				@Override
				public IAndroidAppContext getServiceContext() {
					return context;
				}

				@Override
				public IPage getBindingPage() {
					return page;
				}

				@Override
				public BindableActivity getBindingActivity() {
					return target;
				}
			});
			return binding;
		}else{
			return null;
		}
	}

	@Override
	protected void initServiceDependency() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void startService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void stopService() {
		// TODO Auto-generated method stub
		
	}

}
