/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IPageCallback;
import com.wxxr.mobile.core.ui.api.IPageDescriptor;
import com.wxxr.mobile.core.ui.api.IPageNavigator;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

/**
 * @author neillin
 *
 */
public abstract class WorkbenchBase implements IWorkbench {
	private static final Trace log = Trace.register(WorkbenchBase.class);
	
	private final IWorkbenchRTContext uiContext;
	
//	private String activePageId;
	private Map<String, IPage> pages = new HashMap<String, IPage>();
	
		
	public WorkbenchBase(IWorkbenchRTContext ctx) {
		this.uiContext = ctx;
		init();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#getActivePageId()
	 */
	public String getActivePageId() {
		IPage page = uiContext.getWorkbenchManager().getPageNavigator().getCurrentActivePage();
		return page != null ? page.getName() : null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#getPage(java.lang.String)
	 */
	public IPage getPage(String pageId) {
		IPage page = this.pages.get(pageId);
		if(page == null){
			IPageDescriptor desc = uiContext.getWorkbenchManager().getPageDescriptor(pageId);
			if(desc != null){
				page = (IPage)desc.createPresentationModel(uiContext);
				page.init(uiContext);
				this.pages.put(pageId, page);
			}
		}
		return page;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#showPage(java.lang.String)
	 */
	public void showPage(String pageId,Map<String, Object> params,IPageCallback callback) {
		if(log.isTraceEnabled()){
			log.trace("Going to show page :"+pageId);
		}
		if(pageId.equals(getActivePageId())){
			return;
		}
		IPage page = getPage(pageId);
		if(page != null){
			if((params != null)&&(params.isEmpty() == false)){
				IModelUpdater updater = page.getAdaptor(IModelUpdater.class);
				if(updater != null){
					params = new HashMap<String, Object>(params);
					updater.updateModel(params);
				}
			}
			IPageNavigator nav = this.uiContext.getWorkbenchManager().getPageNavigator();
			nav.showPage(page,params,callback);
//			this.activePageId = nav.getCurrentActivePage() != null ? nav.getCurrentActivePage().getName() : null;
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#showHomePage()
	 */
	public void showHomePage() {
		showPage(HOME_PAGE_ID,null,null);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#hidePage(java.lang.String)
	 */
	public void hidePage(String pageId) {
		IPage page = getPage(pageId);
		if(page != null){
			IPageNavigator nav = this.uiContext.getKernelContext().getService(IPageNavigator.class);
			nav.hidePage(page);
//			this.activePageId = nav.getCurrentActivePage().getName();
		}
		
	}
	
	protected IWorkbenchRTContext getUIContext() {
		return this.uiContext;
	}
	
	/**
	 * subclass could override this method to initialize workbench
	 */
	protected void init(){
		
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#showMessageBox(java.lang.String, java.util.Map)
	 */
	public void showMessageBox(String message, Map<String, Object> params) {
		params = params != null ? new HashMap<String, Object>(params) : new HashMap<String, Object>();
		params.put(MESSAGE_BOX_MESSAGE_ID, message);
		showPage(MESSAGE_BOX_ID, params, null);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#createNInitializedView(java.lang.String)
	 */
	@Override
	public IView createNInitializedView(String viewId) {
		IViewDescriptor desc = getUIContext().getWorkbenchManager().getViewDescriptor(viewId);
		if(desc != null){
			IView view = desc.createPresentationModel(getUIContext());
			if(!view.isInitialized()){
				view.init(getUIContext());
			}
			return view;
		}
		return null;
	}	

}
