/**
 * 
 */
package com.wxxr.mobile;

import android.app.Application;

import com.wxxr.mobile.android.app.AndroidFramework;
import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.android.app.IAndroidFramework;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;

/**
 * @author fudapeng
 *
 */
public class DemoAppFramework extends AndroidFramework<IAndroidAppContext, AbstractModule<IAndroidAppContext>> {

	private final DemoApplication app;
	
	IAndroidAppContext a;
	private class ComHelperAppContextImpl extends AbstractContext implements IAndroidAppContext {

		@SuppressWarnings("rawtypes")
		@Override
		public IAndroidFramework getApplication() {
			return DemoAppFramework.this;
		}
		
	};
	
	private ComHelperAppContextImpl context;
	public DemoAppFramework(DemoApplication demoApplication) {
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
		registerKernelModule(new com.wxxr.mobile.module.DemoWorkbenchManagerModule());
		registerKernelModule(new com.wxxr.mobile.service.TimeService());
	}

}
