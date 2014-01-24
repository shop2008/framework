/**
 * 
 */
package com.wxxr.archetype.mobile;

import android.app.Application;

import com.wxxr.mobile.android.app.AndroidFramework;
import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.android.app.IAndroidFramework;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;

/**
 * @author fudapeng
 *
 */
public class AppFramework extends AndroidFramework<IAndroidAppContext, AbstractModule<IAndroidAppContext>> {

	private final MobileApplication app;
	
	IAndroidAppContext a;
	private class ComHelperAppContextImpl extends AbstractContext implements IAndroidAppContext {

		@SuppressWarnings("rawtypes")
		@Override
		public IAndroidFramework getApplication() {
			return AppFramework.this;
		}
		
	};
	
	private ComHelperAppContextImpl context;
	public AppFramework(MobileApplication demoApplication) {
		this.app = demoApplication;
	}
	

	@Override
	public Application getAndroidApplication() {
		return app;
	}

	@Override
	public String getApplicationName() {
		return "mobile demo";
	}

	@Override
	protected IAndroidAppContext getContext() {
		if(context == null)
			context = new ComHelperAppContextImpl();
		return context;
	}

	@Override
	protected void initModules() {
		registerKernelModule(new com.wxxr.archetype.mobile.module.WorkbenchManagerModule());
		registerKernelModule(new com.wxxr.archetype.mobile.service.TimeService());
	}

}
