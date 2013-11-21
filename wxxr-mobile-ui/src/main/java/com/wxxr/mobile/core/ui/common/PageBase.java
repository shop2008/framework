/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.List;

import com.wxxr.mobile.core.ui.api.IAppToolbar;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewGroup;


/**
 * @author neillin
 *
 */
public abstract class PageBase extends ViewBase implements IPage {

	private IAppToolbar toolbar;

	public PageBase() {
		super();
	}



	public PageBase(String name) {
		super(name);
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
	 * @see com.wxxr.mobile.core.ui.api.IPage#getViewFrame(java.lang.String)
	 */
	public IViewGroup getViewGroup(String name) {
		return getChild(name, IViewGroup.class);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPage#getAllFrames()
	 */
	public List<IViewGroup> getAllViewGroups() {
		return getChildren(IViewGroup.class);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPage#showView(java.lang.String)
	 */
	public void showView(String viewName,boolean backable) {
		for (IViewGroup grp : getAllViewGroups()) {
			if(grp.hasView(viewName)){
				grp.activateView(viewName,backable);
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPage#hideView(java.lang.String)
	 */
	public void hideView(String viewName) {
		for (IViewGroup grp : getAllViewGroups()) {
			if(grp.hasView(viewName)){
				grp.deactivateView(viewName);
				break;
			}
		}
	}



	public IView getView(String viewId) {
		for (IViewGroup grp : getAllViewGroups()) {
			if(grp.hasView(viewId)){
				return grp.getView(viewId);
			}
		}
		return null;
	}



	public IViewGroup createViewGroup(String grpName) {
		ViewGroupBase vg = new ViewGroupBase();
		vg.setName(grpName);
		addChild(vg);
		return vg;
	}



	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPage#onToolbarCreated(com.wxxr.mobile.core.ui.api.IAppToolbar)
	 */
	@Override
	public void onToolbarCreated(IAppToolbar toolbar) {
		this.toolbar = toolbar;
	}



	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPage#onToolbarShow()
	 */
	@Override
	public void onToolbarShow() {		
	}



	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPage#onToolbarHide()
	 */
	@Override
	public void onToolbarHide() {
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPage#getPageToolbar()
	 */
	@Override 
	public IAppToolbar getPageToolbar() {
		return this.toolbar;
	}

	protected IAppToolbar getAppToolbar() {
		return this.toolbar;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPage#onToolbarDestroy()
	 */
	@Override
	public void onToolbarDestroy() {
		if(this.toolbar != null){
			this.toolbar = null;
		}
	}



	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.ViewBase#show()
	 */
	@Override
	public void show() {
		getUIContext().getWorkbenchManager().getPageNavigator().showPage(this, null);
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.ViewBase#hide()
	 */
	@Override
	public void hide() {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
	}
	
}
