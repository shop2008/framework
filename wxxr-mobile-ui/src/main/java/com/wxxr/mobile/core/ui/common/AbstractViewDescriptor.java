/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.core.ui.api.IBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.TargetUISystem;
import com.wxxr.mobile.core.ui.api.ViewType;


/**
 * @author neillin
 *
 */
public abstract class AbstractViewDescriptor implements IViewDescriptor {
	
	private String viewId, viewName, viewDescription;
	private ViewType viewType;
	private boolean isSingleton;
	private Map<TargetUISystem, IBindingDescriptor> bindingDescriptiors;
	private IView singleton;
	
	public AbstractViewDescriptor(){
		init();
	}
	/**
	 * @return the viewId
	 */
	public String getViewId() {
		return viewId;
	}
	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}
	/**
	 * @return the viewDescriptor
	 */
	public String getViewDescription() {
		return viewDescription;
	}
	/**
	 * @return the viewType
	 */
	public ViewType getViewType() {
		return viewType;
	}
	/**
	 * @return the isSingleton
	 */
	public boolean isSingleton() {
		return isSingleton;
	}
	/**
	 * @param viewId the viewId to set
	 */
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	/**
	 * @param viewDescriptor the viewDescriptor to set
	 */
	public void setViewDescription(String text) {
		this.viewDescription = text;
	}
	/**
	 * @param viewType the viewType to set
	 */
	public void setViewType(ViewType viewType) {
		this.viewType = viewType;
	}
	/**
	 * @param isSingleton the isSingleton to set
	 */
	public void setSingleton(boolean isSingleton) {
		this.isSingleton = isSingleton;
	}
	
	public IBindingDescriptor getBindingDescriptor(TargetUISystem targetSystem) {
		return this.bindingDescriptiors != null ? this.bindingDescriptiors.get(targetSystem) : null;
	}
	
	public IView createPresentationModel(IWorkbenchRTContext ctx) {
		if(isSingleton()){
			if(this.singleton == null){
				this.singleton = createPModel(ctx);
			}
			return this.singleton;
		}
		return createPModel(ctx);
	}
	
	protected AbstractViewDescriptor addBindingDescriptor(TargetUISystem target, IBindingDescriptor descriptor){
		if(this.bindingDescriptiors == null){
			this.bindingDescriptiors = new HashMap<TargetUISystem, IBindingDescriptor>();
		}
		this.bindingDescriptiors.put(target, descriptor);
		return this;
	}

	protected AbstractViewDescriptor removeBindingDescriptor(TargetUISystem target, IBindingDescriptor descriptor){
		if(this.bindingDescriptiors != null){
			IBindingDescriptor desc = this.bindingDescriptiors.get(target);
			if(desc == descriptor){
				this.bindingDescriptiors.remove(target);
			}
		}
		return this;
	}

	protected abstract IView createPModel(IWorkbenchRTContext ctx);
	
	protected abstract void init();

}
