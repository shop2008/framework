/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.LinkedList;

import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.IViewGroupDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;


/**
 * @author neillin
 *
 */
public class BaseViewGroupDescriptor implements IViewGroupDescriptor {
	private LinkedList<String> viewIds;
	private String id;
	private IViewGroup vg;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IViewGroupDescriptor#addView(java.lang.String)
	 */
	public IViewGroupDescriptor addView(String viewId) {
		if(viewIds == null){
			this.viewIds = new LinkedList<String>();
		}
		if(!this.viewIds.contains(viewId)){
			this.viewIds.add(viewId);
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IViewGroupDescriptor#removeView(java.lang.String)
	 */
	public IViewGroupDescriptor removeView(String viewId) {
		if(this.viewIds != null){
			this.viewIds.remove(viewId);
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IViewGroupDescriptor#getViewIds()
	 */
	public String[] getViewIds() {
		return this.viewIds != null ? this.viewIds.toArray(new String[this.viewIds.size()]) : new String[0];
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IViewGroupDescriptor#createViewGroup(com.wxxr.mobile.core.ui.api.IWorkbenchRTContext)
	 */
	public IViewGroup createViewGroup(IWorkbenchRTContext ctx) {
		if(this.vg == null){
			this.vg = new LazyLoadViewGroup(getId(),getViewIds());
			vg.init(ctx);
		}
		return vg;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

}
