/**
 * 
 */
package com.wxxr.mobile.core.ui.impl;

import java.util.List;

import com.wxxr.mobile.core.ui.api.DataChangedEvent;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IUIManagementContext;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewFrame;

/**
 * @author neillin
 *
 */
public abstract class PageBase extends AbstractUIContainer implements IPage {


	private IBinding binding;
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#notifyDataChanged(com.wxxr.mobile.core.ui.api.DataChangedEvent)
	 */
	public void notifyDataChanged(DataChangedEvent event) {
		if(this.binding != null){
			this.binding.notifyDataChanged(event);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IAdaptable#getAdaptor(java.lang.Class)
	 */
	public <T> T getAdaptor(Class<T> clazz) {
		if(clazz == IPage.class){
			return clazz.cast(this);
		}
		return super.getAdaptor(clazz);
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBindable#addBinding(com.wxxr.mobile.core.ui.api.IBinding)
	 */
	public void addBinding(IBinding binding) {
		if(binding == null){
			throw new IllegalArgumentException("Invalid binding NULL !");
		}
		if(this.binding == null){
			this.binding = binding;
		}else{
			throw new IllegalStateException("Page could only take one binding, exist binding :"+this.binding);
		}

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBindable#removeBinding(com.wxxr.mobile.core.ui.api.IBinding)
	 */
	public boolean removeBinding(IBinding binding) {
		if(this.binding == binding){
			this.binding = null;
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPage#getViewFrame(java.lang.String)
	 */
	public IViewFrame getViewFrame(String name) {
		return getChild(name, IViewFrame.class);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPage#getMainViewFrame()
	 */
	public IViewFrame getMainViewFrame() {
		return getViewFrame("main");
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPage#getAllFrames()
	 */
	public List<IViewFrame> getAllFrames() {
		return getChildren(IViewFrame.class);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPage#showView(java.lang.String)
	 */
	public void showView(String viewName) {
		for (IViewFrame frame : getAllFrames()) {
			IView v = frame.getView(viewName);
			if(v != null){
				frame.activateView(viewName);
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPage#hideView(java.lang.String)
	 */
	public void hideView(String viewName) {
		for (IViewFrame frame : getAllFrames()) {
			IView v = frame.getView(viewName);
			if(v != null){
				frame.deactivateView(viewName);
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.impl.AbstractUIContainer#destroy()
	 */
	@Override
	public void destroy() {
		if(this.binding != null){
			this.binding.deactivate();
			this.binding.destroy();
			this.binding = null;
		}
		super.destroy();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.impl.AbstractUIComponent#init()
	 */
	@Override
	public void init(IUIManagementContext ctx) {
		super.init(ctx);
		for (String frmName : getFrameNames()) {
			IViewFrame frm = ctx.createViewFrame(frmName);
			for (String viewId : getViewIdsOfFrame(frmName)) {
				IView view = ctx.createView(viewId);
				view.init(ctx);
				frm.addView(view);
			}
			add((AbstractUIComponent)frm);
		}
	}

	protected abstract String[] getFrameNames();
	protected abstract String[] getViewIdsOfFrame(String frmName);
}
