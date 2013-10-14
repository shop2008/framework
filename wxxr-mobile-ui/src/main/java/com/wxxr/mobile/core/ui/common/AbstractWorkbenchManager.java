/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.LinkedList;
import java.util.List;

import com.wxxr.mobile.core.event.api.IEventRouter;
import com.wxxr.mobile.core.ui.api.IEventBinderManager;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;
import com.wxxr.mobile.core.ui.api.IFieldBinderManager;
import com.wxxr.mobile.core.ui.api.IPageDescriptor;
import com.wxxr.mobile.core.ui.api.IPageNavigator;
import com.wxxr.mobile.core.ui.api.IUICommandExecutor;
import com.wxxr.mobile.core.ui.api.IViewBinder;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.UISystemInitEvent;

/**
 * @author neillin
 *
 */
public abstract class AbstractWorkbenchManager implements IWorkbenchManager {
	
	private GenericContainer<IViewDescriptor> viewContainer;
	
	private IWorkbenchRTContext context;
	
	private IUICommandExecutor commandExecutor;
	
	private IFieldAttributeManager fieldAttributeManager;
	
	private IWorkbench workbench;
	
	private IPageNavigator navigator;
	
	private IViewBinder viewBinder;
	
	private IFieldBinderManager fieldBinderManager; // = new SimpleFieldBinderManager<Context>(uiContext);

	private IEventBinderManager eventBinderManager; // = new SimpleEventBinderManager<Context, View>(uiContext);

	
	public AbstractWorkbenchManager registerView(IViewDescriptor descriptor){
		this.viewContainer.add(descriptor);
		return this;
	}
	
	
	public AbstractWorkbenchManager unregisterView(String name){
		this.viewContainer.remove(name);
		return this;
	}
	
	private <T extends IViewDescriptor> List<T> getChildren(Class<T> clazz) {
		return this.viewContainer.getChildren(clazz);
	}

	private <T extends IViewDescriptor> T getChild(String name, Class<T> clazz) {
		return this.viewContainer.getChild(name, clazz);
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#getPageIds()
	 */
	public String[] getAllRegisteredPageIds() {
		List<String> ids = null;
		List<IPageDescriptor> pages = getChildren(IPageDescriptor.class);
		for (IPageDescriptor iPage : pages) {
			if(ids == null){
				ids = new LinkedList<String>();
			}
			ids.add(iPage.getViewId());
		}
		return ids != null ? ids.toArray(new String[ids.size()]) : null;
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbenchManager#getPageDescriptor(java.lang.String)
	 */
	public IPageDescriptor getPageDescriptor(String pageId) {
		return getChild(pageId, IPageDescriptor.class);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbenchManager#getViewDescriptor(java.lang.String)
	 */
	public IViewDescriptor getViewDescriptor(String viewId) {
		return getChild(viewId, IViewDescriptor.class);
	}




	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbenchManager#getAllRegisteredViewIds()
	 */
	public String[] getAllRegisteredViewIds() {
		List<String> ids = null;
		List<IViewDescriptor> views = getChildren(IViewDescriptor.class);
		for (IViewDescriptor v : views) {
			if( v instanceof IPageDescriptor){
				continue;
			}
			if(ids == null){
				ids = new LinkedList<String>();
			}
			ids.add(v.getViewId());
		}
		return ids != null ? ids.toArray(new String[ids.size()]) : null;
	}
	
	public void init(IWorkbenchRTContext ctx){
		this.context = ctx;
		this.viewContainer = new GenericContainer<IViewDescriptor>() {
			
			@Override
			protected void handleRemoved(IViewDescriptor child) {
			}
			
			@Override
			protected void handleDestroy(IViewDescriptor ui) {
			}
			
			@Override
			protected void handleAdded(IViewDescriptor child) {
			}
			
			@Override
			protected String getObjectId(IViewDescriptor child) {
				return child.getViewId();
			}
		};
	}
	
	public void destroy() {
		if(this.viewContainer != null){
			this.viewContainer.destroy();
			this.viewContainer = null;
		}
		this.context = null;
	}
	
	public void notifyUIInitEvent(){
		this.context.getKernelContext().getService(IEventRouter.class).routeEvent(new UISystemInitEvent(context));
	}



	protected IUICommandExecutor createCommandExecutor() {
		return new SimpleCommandExecutor(context);
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbenchManager#getCommandExecutor()
	 */
	public IUICommandExecutor getCommandExecutor() {
		if(this.commandExecutor == null){
			this.commandExecutor = createCommandExecutor();
		}
		return this.commandExecutor;
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbenchManager#getFieldAttributeManager()
	 */
	public IFieldAttributeManager getFieldAttributeManager() {
		if(this.fieldAttributeManager == null){
			this.fieldAttributeManager = createFieldAttributeManager();
		}
		return this.fieldAttributeManager;
	}
	
	protected IFieldAttributeManager createFieldAttributeManager(){
		return new SimpleFieldAttributeManager();
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbenchManager#getWorkbench()
	 */
	@Override
	public IWorkbench getWorkbench() {
		if(workbench == null){
			workbench = createWorkbench();
		}
		return workbench;
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbenchManager#getPageNavigator()
	 */
	@Override
	public IPageNavigator getPageNavigator() {
		if(navigator == null){
			navigator = createPageNavigator();
		}
		return navigator;
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbenchManager#getFieldBinderManager()
	 */
	@Override
	public IFieldBinderManager getFieldBinderManager() {
		if(fieldBinderManager == null){
			this.fieldBinderManager = createFieldBinderManager();
		}
		return fieldBinderManager;
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbenchManager#getEventBinderManager()
	 */
	@Override
	public IEventBinderManager getEventBinderManager() {
		if(eventBinderManager == null){
			eventBinderManager = createEventBinderManager();
		}
		return eventBinderManager;
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbenchManager#getViewBinder()
	 */
	@Override
	public IViewBinder getViewBinder() {
		if(viewBinder == null){
			viewBinder = createViewBinder();
		}
		return viewBinder;
	}
	
	protected abstract IViewBinder createViewBinder();
	
	protected IEventBinderManager createEventBinderManager() {
		return new SimpleEventBinderManager(context);
	}
	
	protected IFieldBinderManager createFieldBinderManager(){
		return new SimpleFieldBinderManager(context);
	}
	
	protected abstract IPageNavigator createPageNavigator();
	
	protected abstract IWorkbench createWorkbench();
}
