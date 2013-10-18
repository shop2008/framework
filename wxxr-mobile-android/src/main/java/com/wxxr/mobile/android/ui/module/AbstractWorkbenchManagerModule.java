/**
 * 
 */
package com.wxxr.mobile.android.ui.module;

import android.view.View;

import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.android.ui.AbstractAndroidWorkbenchManager;
import com.wxxr.mobile.android.ui.AndroidPageNavigator;
import com.wxxr.mobile.android.ui.IAndroidWorkbenchManager;
import com.wxxr.mobile.android.ui.SimpleAndroidViewBinder;
import com.wxxr.mobile.android.ui.binding.SimpleFieldBinder;
import com.wxxr.mobile.core.event.api.IEventRouter;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.ui.api.IEventBinderManager;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;
import com.wxxr.mobile.core.ui.api.IFieldBinderManager;
import com.wxxr.mobile.core.ui.api.IPageNavigator;
import com.wxxr.mobile.core.ui.api.IViewBinder;
import com.wxxr.mobile.core.ui.api.IWorkbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.common.UIComponent;
import com.wxxr.mobile.core.ui.common.WorkbenchBase;

/**
 * @author neillin
 *
 */
public abstract class AbstractWorkbenchManagerModule<T extends IAndroidAppContext> extends AbstractModule<T> {

	protected final IWorkbenchRTContext uiContext = new IWorkbenchRTContext() {
		
		public IKernelContext getKernelContext() {
			return context;
		}
		

		public IWorkbenchManager getWorkbenchManager() {
			return manager;
		}


		public Object getDomainModel(String modelName) {
			//TODO
			return null;
		}
	};

	protected final AbstractAndroidWorkbenchManager manager = new AbstractAndroidWorkbenchManager() {

		@Override
		protected IViewBinder createViewBinder() {
			return new SimpleAndroidViewBinder(uiContext);
		}

		@Override
		protected IPageNavigator createPageNavigator() {
			return new AndroidPageNavigator(uiContext);
		}

		@Override
		protected IWorkbench createWorkbench() {
			return doCreateWorkbench();
		}
	};


	@Override
	protected void initServiceDependency() {
		addRequiredService(IEventRouter.class);
	}

	@Override
	protected void startService() {
		this.manager.init(uiContext);
		this.manager.getFieldBinderManager().registerFieldBinder(UIComponent.class,View.class, new SimpleFieldBinder());
		initFieldBinders(this.manager.getFieldBinderManager());
		initEventBinders(this.manager.getEventBinderManager());
		initAttributeUpdaters(this.manager.getFieldAttributeManager());
		initPresentationModels(uiContext);
		context.registerService(IAndroidWorkbenchManager.class, manager);
	}

	@Override
	protected void stopService() {
		context.unregisterService(IAndroidWorkbenchManager.class, manager);
		this.manager.destroy();
	}

	protected abstract void initFieldBinders(IFieldBinderManager mgr);

	protected abstract void initEventBinders(IEventBinderManager mgr);

	protected abstract void initAttributeUpdaters(IFieldAttributeManager mgr);
	
	protected abstract void initPresentationModels(IWorkbenchRTContext context);
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#notifyKernelStarted()
	 */
	@Override
	protected void notifyKernelStarted() {
		super.notifyKernelStarted();
		manager.notifyUIInitEvent();
	}

	/**
	 * @return
	 */
	protected IWorkbench doCreateWorkbench() {
		return new WorkbenchBase(uiContext) {
			
			@Override
			public String[] getPageIds() {
				return manager.getAllRegisteredPageIds();
			}
		};
	}


}
