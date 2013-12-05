/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.ui.api.IDialog;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IPageNavigator;
import com.wxxr.mobile.core.ui.api.ISelectionService;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewNavigationCallback;
import com.wxxr.mobile.core.ui.api.IViewNavigationListener;
import com.wxxr.mobile.core.ui.api.IWorkbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.UIConstants;

/**
 * @author neillin
 *
 */
public abstract class WorkbenchBase implements IWorkbench {
	private static final Trace log = Trace.register(WorkbenchBase.class);
	
	private final IWorkbenchRTContext uiContext;
	private final SelectionServiceSupport selectionService = new SelectionServiceSupport();
	private Stack<IView> onShowViews = new Stack<IView>();
	private List<IView> createdViews = new ArrayList<IView>();
	private IViewNavigationListener listener = new IViewNavigationListener() {
		
		@Override
		public void onShow(final IView view) {
			synchronized(onShowViews){
				onShowViews.remove(view);
				onShowViews.push(view);
				KUtils.runOnUIThread(new Runnable() {
					
					@Override
					public void run() {
						view.processStartupExceptions();
					}
				}, 0, null);
			}
		}
		
		@Override
		public void onHide(IView view) {
			synchronized(onShowViews){
				onShowViews.remove(view);
			}
		}
		
		@Override
		public void onDestroy(IView view) {
			synchronized(createdViews){
				createdViews.remove(view);
			}
		}
		
		@Override
		public void onCreate(IView view) {
			synchronized(createdViews){
				if(!createdViews.contains(view)){
					createdViews.add(view);
				}
			}
		}

		@Override
		public boolean acceptable(IView view) {
			return true;
		}
		
	};
	
//	private String activePageId;
	private Map<String, IPage> pages = new HashMap<String, IPage>();
	
		
	public WorkbenchBase(IWorkbenchRTContext ctx) {
		this.uiContext = ctx;
		IPageNavigator nav = ctx.getWorkbenchManager().getPageNavigator();
		nav.registerNavigationListener(listener);
		init();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#getPage(java.lang.String)
	 */
	public IPage getPage(String pageId) {
		IPage page = this.pages.get(pageId);
		if(page == null){
			AbstractPageDescriptor desc = (AbstractPageDescriptor)uiContext.getWorkbenchManager().getPageDescriptor(pageId);
			if(desc != null){
				page = (PageBase)desc.createPresentationModel(uiContext);
				((PageBase)page).init(uiContext);
				this.pages.put(pageId, page);
			}
		}
		return page;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#showPage(java.lang.String)
	 */
	public void showPage(String pageId,Map<String, Object> params,IViewNavigationCallback callback) {
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
				}else{
					for (Entry<String, Object> entry : params.entrySet()) {
						page.setProperty(entry.getKey(), entry.getValue());
					}
				}
			}
			IPageNavigator nav = this.uiContext.getWorkbenchManager().getPageNavigator();
			nav.showPage(page,callback);
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
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#createNInitializedView(java.lang.String)
	 */
	@Override
	public IView createNInitializedView(String viewId) {
		AbstractViewDescriptor<ViewBase> desc = (AbstractViewDescriptor<ViewBase>)getUIContext().getWorkbenchManager().getViewDescriptor(viewId);
		if(desc != null){
			ViewBase view = desc.createPresentationModel(getUIContext());
			if(!view.isInitialized()){
				view.init(getUIContext());
			}
			return view;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#createDialog(java.lang.String, java.util.Map)
	 */
	@Override
	public IDialog createDialog(String viewId, Map<String, Object> params) {
		AbstractViewDescriptor<ViewBase> desc = (AbstractViewDescriptor<ViewBase>)getUIContext().getWorkbenchManager().getViewDescriptor(viewId);
		if(desc != null){
			ViewBase view = desc.createPresentationModel(getUIContext());
			if(!view.isInitialized()){
				view.init(getUIContext());
			}
			IModelUpdater updater = view.getAdaptor(IModelUpdater.class);
			if(updater != null){
				updater.updateModel(params);
			}
			return getUIContext().getWorkbenchManager().getPageNavigator().createDialog(view,params.get(UIConstants.MESSAGEBOX_ATTRIBUTE_HANDBACK));
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#initComponent(com.wxxr.mobile.core.ui.api.IUIComponent)
	 */
	@Override
	public void initComponent(IUIComponent component) {
		((UIComponent)component).init(getUIContext());
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#destroyComponent(com.wxxr.mobile.core.ui.api.IUIComponent)
	 */
	@Override
	public void destroyComponent(IUIComponent component) {
		((UIComponent)component).destroy();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#getSelectionService()
	 */
	@Override
	public ISelectionService getSelectionService() {
		return this.selectionService;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbench#getPageIds()
	 */
	@Override
	public String[] getPageIds() {
		return this.uiContext.getWorkbenchManager().getAllRegisteredPageIds();
	}

	@Override
	public IView getActiveView() {
		synchronized(onShowViews){
			return onShowViews.isEmpty() ?  null : onShowViews.peek();
		}
	}
	
	@Override
	public String getActivePageId() {
		synchronized(onShowViews){
			if(onShowViews.isEmpty()){
				return null;
			}
			int size = onShowViews.size();
			for(int i=size-1;i >= 0; i--){
				IView v = onShowViews.get(i);
				if(v instanceof IPage){
					return v.getName();
				}
			}
			return null;
		}
	}

}
