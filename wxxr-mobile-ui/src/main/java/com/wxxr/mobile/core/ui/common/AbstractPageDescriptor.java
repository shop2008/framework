/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IPageDescriptor;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewGroupDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;


/**
 * @author neillin
 *
 */
public abstract class AbstractPageDescriptor extends AbstractViewDescriptor implements
		IPageDescriptor {
	
	private Map<String, IViewGroupDescriptor> vgDescriptors;// = new HashMap<String, IViewGroupDescriptor>();
	
	public AbstractPageDescriptor(){
		setSingleton(true);
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPageDescriptor#createViewGroup(java.lang.String)
	 */
	public IViewGroupDescriptor createViewGroup(String name) {
		if(this.vgDescriptors == null){
			this.vgDescriptors = new HashMap<String, IViewGroupDescriptor>();
		}
		IViewGroupDescriptor vg = this.vgDescriptors.get(name);
		if(vg == null){
			BaseViewGroupDescriptor bvg = new BaseViewGroupDescriptor();
			bvg.setId(name);
			this.vgDescriptors.put(name, bvg);
			vg = bvg;
		}
		return vg;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPageDescriptor#removeViewGroup(java.lang.String)
	 */
	public IViewGroupDescriptor removeViewGroup(String name) {
		return this.vgDescriptors != null ? this.vgDescriptors.remove(name) : null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPageDescriptor#getAllViewGroupNames()
	 */
	public String[] getAllViewGroupNames() {
		return this.vgDescriptors != null ? this.vgDescriptors.keySet().toArray(new String[this.vgDescriptors.size()]) : new String[0];
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPageDescriptor#getViewGroup(java.lang.String)
	 */
	public IViewGroupDescriptor getViewGroup(String name) {
		return  this.vgDescriptors != null ? this.vgDescriptors.get(name) : null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.AbstractViewDescriptor#createPModel(com.wxxr.mobile.core.ui.api.IWorkbenchRTContext)
	 */
	@Override
	protected IView createPModel(IWorkbenchRTContext ctx) {
		IPage model = createPageModel(ctx);
		for (String vgName : getAllViewGroupNames()) {
			IViewGroupDescriptor vgDesc = getViewGroup(vgName);
			model.addChild(vgDesc.createViewGroup(ctx));
		}
		return model;
	}
	
	protected abstract IPage createPageModel(IWorkbenchRTContext ctx);
	
}
