/**
 * 
 */
package com.wxxr.mobile.android.ui.module;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.android.ui.AbstractAndroidWorkbenchManager;
import com.wxxr.mobile.android.ui.AndroidPageNavigator;
import com.wxxr.mobile.android.ui.IAndroidWorkbenchManager;
import com.wxxr.mobile.android.ui.SimpleAndroidViewBinder;
import com.wxxr.mobile.android.ui.binding.AdapterViewFieldBinder;
import com.wxxr.mobile.android.ui.binding.ClickEventBinder;
import com.wxxr.mobile.android.ui.binding.ItemClickEventBinder;
import com.wxxr.mobile.android.ui.binding.SimpleFieldBinder;
import com.wxxr.mobile.android.ui.binding.UICommandButtonBinder;
import com.wxxr.mobile.android.ui.binding.ViewGroupFieldBinder;
import com.wxxr.mobile.android.ui.updater.BackgroundColorAttributeUpdater;
import com.wxxr.mobile.android.ui.updater.BackgroupImageURIAttributeUpdater;
import com.wxxr.mobile.android.ui.updater.CheckBoxAttributeUpdater;
import com.wxxr.mobile.android.ui.updater.EnabledAttributeUpdater;
import com.wxxr.mobile.android.ui.updater.ImageURIAttributeUpdater;
import com.wxxr.mobile.android.ui.updater.TextAttributeUpdater;
import com.wxxr.mobile.android.ui.updater.TextColorAttributeUpdater;
import com.wxxr.mobile.android.ui.updater.VisibleAttributeUpdater;
import com.wxxr.mobile.android.ui.updater.WebUrlAttributeUpdater;
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
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.UICommand;
import com.wxxr.mobile.core.ui.common.UIComponent;
import com.wxxr.mobile.core.ui.common.ViewGroupBase;
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

	};


	@Override
	protected void initServiceDependency() {
		addRequiredService(IEventRouter.class);
	}

	@Override
	protected void startService() {
		this.manager.init(uiContext);
		initDefaultFieldBinders(this.manager.getFieldBinderManager());
		initFieldBinders(this.manager.getFieldBinderManager());
		initDefaultEventBinders(this.manager.getEventBinderManager());
		initEventBinders(this.manager.getEventBinderManager());
		initDefaultAttributeUpdaters(this.manager.getFieldAttributeManager());
		initAttributeUpdaters(this.manager.getFieldAttributeManager());
		initBindingDecorators(this.manager.getBindingDecoratorRegistry());
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
	
	protected abstract void initBindingDecorators(IBindingDecoratorRegistry registry);

	protected abstract void initAttributeUpdaters(IFieldAttributeManager mgr);
	
	protected abstract void initPresentationModels(IWorkbenchRTContext context);
	
	protected void initDefaultFieldBinders(IFieldBinderManager mgr){
		mgr.registerFieldBinder(UIComponent.class,View.class, new SimpleFieldBinder());
		mgr.registerFieldBinder(UIComponent.class,AdapterView.class, new AdapterViewFieldBinder());
		mgr.registerFieldBinder(ViewGroupBase.class,ViewGroup.class, new ViewGroupFieldBinder());
		mgr.registerFieldBinder(UICommand.class,Button.class, new UICommandButtonBinder());
	}
	
	protected void initDefaultEventBinders(IEventBinderManager mgr){
		mgr.registerFieldBinder(InputEvent.EVENT_TYPE_CLICK, new ClickEventBinder());
		mgr.registerFieldBinder(InputEvent.EVENT_TYPE_ITEM_CLICK, new ItemClickEventBinder());
	}
	
	protected void initDefaultAttributeUpdaters(IFieldAttributeManager mgr) {
		mgr.registerAttributeUpdater("imageURI", new ImageURIAttributeUpdater());
		mgr.registerAttributeUpdater("backgroundColor", new BackgroundColorAttributeUpdater());
		mgr.registerAttributeUpdater("backgroundImageURI", new BackgroupImageURIAttributeUpdater());
		mgr.registerAttributeUpdater("enabled", new EnabledAttributeUpdater());
		mgr.registerAttributeUpdater("visible", new VisibleAttributeUpdater());
		mgr.registerAttributeUpdater("text", new TextAttributeUpdater());
		mgr.registerAttributeUpdater("textColor", new TextColorAttributeUpdater());
		mgr.registerAttributeUpdater("webUrl", new WebUrlAttributeUpdater());
		mgr.registerAttributeUpdater("checked", new CheckBoxAttributeUpdater());
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#notifyKernelStarted()
	 */
	@Override
	protected void notifyKernelStarted() {
		super.notifyKernelStarted();
		manager.notifyUIInitEvent();
	}

}
