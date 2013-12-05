/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Arrays;
import java.util.LinkedList;

import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public class LazyLoadViewGroup extends ViewGroupBase {
	private LinkedList<String> viewIds;
	private boolean dynamic;
	
	public LazyLoadViewGroup() {
		super();
	}

	public LazyLoadViewGroup(String name) {
		super(name);
	}

	public LazyLoadViewGroup(String grpId, String[] vids){
		if((vids != null)&&(vids.length > 0)){
			this.viewIds = new LinkedList<String>(Arrays.asList(vids));
		}
		setName(grpId);
	}

	public void addViewId(String viewId){
		if(StringUtils.isBlank(viewId)){
			throw new IllegalArgumentException("Invalid view id : NULL");
		}
		if(this.viewIds == null){
			this.viewIds = new LinkedList<String>();
		}
		if(!this.viewIds.contains(viewId)){
			this.viewIds.add(viewId);
		}
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ViewGroupBase#getViewIds()
	 */
	@Override
	public String[] getViewIds() {
		return this.viewIds != null ? this.viewIds.toArray(new String[0]) : null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ViewGroupBase#getView(java.lang.String)
	 */
	@Override
	public IView getView(String name) {
		if((this.dynamic == false)&&(!hasView(name))){
			return null;
		}
		IView v = super.getView(name);
		if(v == null){
			v = getUIContext().getWorkbenchManager().getWorkbench().createNInitializedView(name);
			addChild(v);
		}
		return v;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ViewGroupBase#hasView(java.lang.String)
	 */
	@Override
	public boolean hasView(String name) {
		if(this.viewIds == null){
			return false;
		}
		for (String vid : this.viewIds) {
			if(vid.equals(name)){
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the dynamic
	 */
	protected boolean isDynamic() {
		return dynamic;
	}

	/**
	 * @param dynamic the dynamic to set
	 */
	protected void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

}
