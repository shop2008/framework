/**
 * 
 */
package com.wxxr.mobile.core.ui.impl;

import java.util.Map;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IPageCallback;
import com.wxxr.mobile.core.ui.api.IPageNavigator;
import com.wxxr.mobile.core.ui.api.IUIManagementContext;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewFrame;
import com.wxxr.mobile.core.ui.api.IWorkbench;

/**
 * @author neillin
 *
 */
public abstract class WorkbenchBase implements IWorkbench {
	private static final Trace log = Trace.register(WorkbenchBase.class);
	
	private final IUIManagementContext uiContext = new IUIManagementContext() {
		
		public IKernelContext getKernelContext() {
			return WorkbenchBase.this.getAppContext();
		}
		
		public IViewFrame createViewFrame(String name) {
			return WorkbenchBase.this.createViewFrame(name);
		}
		
		public IView createView(String name) {
			return WorkbenchBase.this.createView(name);
		}
	};
	
//	private Map<String, Class<? extends IView>> views;
//	private Map<String, ViewReference> singletonViews;
	private String activePageId;
	
	
//	/* (non-Javadoc)
//	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#getPageIds()
//	 */
//	public List<String> getPageIds() {
//		List<String> ids = null;
//		List<IPage> pages = getChildren(IPage.class);
//		for (IPage iPage : pages) {
//			if(ids == null){
//				ids = new LinkedList<String>();
//			}
//			ids.add(iPage.getName());
//		}
//		return ids != null ? ids : Collections.EMPTY_LIST;
//	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#getActivePageId()
	 */
	public String getActivePageId() {
		return this.activePageId;
	}

//	/* (non-Javadoc)
//	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#getPage(java.lang.String)
//	 */
//	public IPage getPage(String pageId) {
//		return getChild(pageId,IPage.class);
//	}
//
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#showPage(java.lang.String)
	 */
	public void showPage(String pageId,Map<String, String> params,IPageCallback callback) {
		if(pageId.equals(activePageId)){
			return;
		}
		IPage page = getPage(pageId);
		if(page != null){
			IPageNavigator nav = this.uiContext.getKernelContext().getService(IPageNavigator.class);
			nav.showPage(page,params,callback);
			this.activePageId = nav.getCurrentActivePage().getName();
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#showHomePage()
	 */
	public void showHomePage() {
		showPage("home",null,null);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#hidePage(java.lang.String)
	 */
	public void hidePage(String pageId) {
		IPage page = getPage(pageId);
		if(page != null){
			IPageNavigator nav = this.uiContext.getKernelContext().getService(IPageNavigator.class);
			nav.hidePage(page);
			this.activePageId = nav.getCurrentActivePage().getName();
		}
		
	}
	
	protected IUIManagementContext getUIContext() {
		return this.uiContext;
	}
	
//	protected void registerView(String name, Class<? extends IView> clazz){
//		if(this.views == null){
//			this.views = new HashMap<String, Class<? extends IView>>();
//		}
//		this.views.put(name, clazz);
//	}
//	
//	protected void registerView(String name, IView singleton){
//		if(this.singletonViews == null){
//			this.singletonViews = new HashMap<String, ViewReference>();
//		}
//		this.singletonViews.put(name, new ViewReference(singleton));
//	}
//
//	
//	
//	protected boolean unregisterView(String name){
//		boolean result1 = this.views != null ? this.views.remove(name) != null : false;
//		boolean result2 = this.singletonViews != null ? this.singletonViews.remove(name) != null : false;
//		return result1||result2;
//	}

	protected abstract IView createView(String viewName);
//	{
//		IView view = this.singletonViews != null ? this.singletonViews.get(viewName) : null;
//		if(view == null){
//			Class<? extends IView> clazz = this.views != null ? this.views.get(viewName) : null;
//			if(clazz != null){
//				try {
//					view = clazz.newInstance();
//				} catch (Throwable e) {
//					log.error("Failed to create view instance of :"+viewName+", class :"+clazz.getName(), e);
//				}
//			}
//		}
//		return view;
//	}
		
	protected abstract IKernelContext getAppContext();
	
	protected abstract IViewFrame createViewFrame(String name);
}
