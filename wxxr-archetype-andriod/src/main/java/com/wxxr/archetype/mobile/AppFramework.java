/**
 * 
 */
package com.wxxr.archetype.mobile;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import android.app.Application;

import com.wxxr.archetype.mobile.module.AppSiteSecurityModule;
import com.wxxr.mobile.android.app.AndroidFramework;
import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.android.app.IAndroidFramework;
import com.wxxr.mobile.android.http.HttpRpcServiceModule;
import com.wxxr.mobile.core.command.impl.CommandExecutorModule;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.rest.RestEasyClientModule;

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
		RestEasyClientModule<IAndroidAppContext> rest = new RestEasyClientModule<IAndroidAppContext>();
		rest.getClient().register(JacksonJsonProvider.class);
		registerKernelModule(rest);
		
		CommandExecutorModule<IAndroidAppContext> cmdExecutor = new CommandExecutorModule<IAndroidAppContext>();
		registerKernelModule(cmdExecutor);
		
		
		HttpRpcServiceModule<IAndroidAppContext> m = new HttpRpcServiceModule<IAndroidAppContext>();
		m.setEnablegzip(false);
		m.setConnectionPoolSize(30);
		registerKernelModule(m);
		
		registerKernelModule(new AppSiteSecurityModule());
		registerKernelModule(new com.wxxr.archetype.mobile.service.TimeService());
	}

}
