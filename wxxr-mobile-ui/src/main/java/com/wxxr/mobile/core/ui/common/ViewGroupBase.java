/**
 * 
 */
package com.wxxr.mobile.core.ui.common;


import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.IViewNavigationListener;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;


/**
 * 在一个view group中可能有多个视图（view），但只能有一个视图是激活的，没有视图的view group必须是隐藏的
 * @author neillin
 *
 */
public class ViewGroupBase extends UIContainer<IView> implements IViewGroup {

	private IViewNavigationListener listener = new IViewNavigationListener() {
		
		@Override
		public void onShow(IView view) {
			String name = view.getName();
			viewStack.remove(name);
			viewStack.push(name);		}
		
		@Override
		public void onHide(IView view) {
			viewStack.remove(view.getName());
		}
		
		@Override
		public void onDestroy(IView view) {
			
		}
		
		@Override
		public void onCreate(IView view) {
			
		}
		
		@Override
		public boolean acceptable(IView view) {
			return containsChild(view);
		}
	};
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.UIContainer#destroy()
	 */
	@Override
	public void destroy() {
		getUIContext().getWorkbenchManager().getPageNavigator().unregisterNavigationListener(listener);
		super.destroy();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.UIContainer#init(com.wxxr.mobile.core.ui.api.IWorkbenchRTContext)
	 */
	@Override
	public void init(IWorkbenchRTContext ctx) {
		super.init(ctx);
		ctx.getWorkbenchManager().getPageNavigator().registerNavigationListener(listener);
	}

	private Stack<String> viewStack = new Stack<String>();
	private String defaultViewId;

	public ViewGroupBase() {
		super();
	}

	public ViewGroupBase(String name) {
		super(name);
	}

	public String[] getViewIds() {
		List<String> ids = null;
		List<IView> pages = getChildren(IView.class);
		for (IView v : pages) {
			if(ids == null){
				ids = new LinkedList<String>();
			}
			ids.add(v.getName());
		}
		return ids != null ? ids.toArray(new String[ids.size()]) : null;
	}

	public String getActiveViewId() {
		String viewId = this.viewStack.isEmpty() ?  null : this.viewStack.peek();
//		if(viewId == null){
//			String[] ids = getViewIds();
//			if((ids != null)&&(ids.length > 0)){
//				viewId = ids[0];
//			}
//		}
		return viewId;
	}

	public boolean isViewOnShow(String name){
		String viewId = this.viewStack.isEmpty() ?  null : this.viewStack.peek();
		return name.equals(viewId);
	}
	
	public void activateView(String name,boolean backable) {
		IView view = getView(name);
		if(view != null){
			view.setProperty("add2BackStack", backable);
			if(isViewOnShow(name)&&view.isActive()){
				return;
			}
			if(this.viewStack.isEmpty()){
				setAttribute(AttributeKeys.visible, true);
			}
			view.show();
			this.viewStack.remove(name);
			this.viewStack.push(name);
		}
	}

	public void deactivateView(String name) {
		IView view = getView(name);
		if(view != null){
			view.hide();
		}
		if(name.equals(getActiveViewId())){
			this.viewStack.pop();
			String nextViewId = getActiveViewId();
			view = nextViewId != null ? getView(nextViewId) : null;
			if(view != null){
				view.show();
			}
		}else{
			this.viewStack.remove(name);
		}
//		if(this.viewStack.isEmpty()){
//			setAttribute(AttributeKeys.visible, false);
//		}
	}

	public IView getView(String name) {
		return (IView)getChild(name);
	}

	public boolean hasView(String name) {
		return getView(name) != null;
	}

	/**
	 * @return the defaultViewId
	 */
	public String getDefaultViewId() {
		return defaultViewId;
	}

	/**
	 * @param defaultViewId the defaultViewId to set
	 */
	public void setDefaultViewId(String defaultViewId) {
		this.defaultViewId = defaultViewId;
	}
	
	public void resetViewStack() {
		if(this.viewStack != null)
			this.viewStack.clear();
	}

}
