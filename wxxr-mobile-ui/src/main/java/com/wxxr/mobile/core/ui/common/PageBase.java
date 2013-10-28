/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.List;

import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewGroup;


/**
 * @author neillin
 *
 */
public abstract class PageBase extends ViewBase implements IPage {

	

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
	public void showView(String viewName) {
		for (IViewGroup grp : getAllViewGroups()) {
			if(grp.hasView(viewName)){
				grp.activateView(viewName);
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

	
}
