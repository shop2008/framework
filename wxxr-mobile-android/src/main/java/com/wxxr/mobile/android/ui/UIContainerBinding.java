/**
 * 
 */
package com.wxxr.mobile.android.ui;

import java.util.LinkedList;
import java.util.Properties;

import android.view.View;
import android.view.ViewGroup;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.DataChangedEvent;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IUIContainer;

/**
 * @author neillin
 *
 */
public abstract class UIContainerBinding extends BasicFieldBinding {
	private static final Trace log = Trace.register(UIContainerBinding.class);
	
	private LinkedList<IFieldBinding> bindings;
	
	public UIContainerBinding(View view, IUIComponent field) {
		super(view, field);
		if(!(view instanceof ViewGroup)){
			throw new IllegalArgumentException("view must be a view group, invalid view :"+view);
		}
		if(!(field instanceof IUIContainer)){
			throw new IllegalArgumentException("field must be a ui container, invalid field :"+field);
		}
	}

	protected void init(ViewGroup vGrp, IUIContainer container,IBindingContext ctx){
		for (IUIComponent view : container) {
			String uiName =  getUIName(view);
			RUtils rUtils = RUtils.getInstance(ctx.getBindingActivity().getPackageName());
			Integer vid = rUtils.getIdResourceId(uiName);
			if(vid != null){
				View ui = vGrp.findViewById(vid.intValue());
				if(ui != null){
					if(UIUtils.isSubsidiary(vGrp, ui)){
						if(log.isDebugEnabled()){
							log.debug("Found UI for field :"+view.getName()+", ui :"+ui);
						}
						IFieldBinding vBinding = createChildBinding(view, ui,vid);
						vBinding.init(ctx);
						if(bindings == null){
							bindings = new LinkedList<IFieldBinding>();
						}
						bindings.add(vBinding);
						if(log.isInfoEnabled()){
							log.info("Field was bound! View id:"+uiName+", field name :"+view.getName());
						}
					}else{
						log.warn("Found UI for field :"+view.getName()+", ui :"+ui+", but it's not child of view group :"+vGrp);
					}
				}else{
					log.warn("Found view id of :"+uiName+", field name :"+view.getName()+", but correspondent UI was not found !");
				}
			}else{
				if(log.isInfoEnabled()){
					log.info("Cannot find view id of :"+uiName+", field name :"+view.getName());
				}
			}
		}
	}

	/**
	 * @param view
	 * @param ui
	 * @return
	 */
	protected IFieldBinding createChildBinding(IUIComponent view, View ui, Integer vid) {
		IFieldBindingFactoryManager mgr = getBindingContext().getServiceContext().
				getService(IFieldBindingFactoryManager.class);
		Properties p = mgr.getTagProperties(vid,ui);
		String factoryName = p.getProperty(BindingContants.PROPERTY_KEY_BINDING_FACTORY_NAME);
		if(factoryName == null){
			factoryName = getChildBindingFactoryName(view,ui);
		}
		return mgr.getBindingStrategy(factoryName).createBinding(getBindingContext(), ui, view);

	}

	protected abstract String getChildBindingFactoryName(IUIComponent view, View ui);
	/**
	 * @param view
	 * @return
	 */
	protected abstract String getUIName(IUIComponent field);
	

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.BasicFieldBinding#init(com.wxxr.mobile.android.ui.IBindingContext)
	 */
	@Override
	public void init(IBindingContext ctx) {
		super.init(ctx);
		init((ViewGroup)getComponent(), (IUIContainer)getField(),ctx);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.BasicFieldBinding#updateUI()
	 */
	@Override
	public void updateUI(boolean recursive) {
		super.updateUI(false);
		if(!isVisible()){
			return;
		}
		if(recursive && (this.bindings != null)){
			for (IFieldBinding binding : this.bindings) {
				binding.updateUI(recursive);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.BasicFieldBinding#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		if(this.bindings != null){
			for (IFieldBinding binding : this.bindings) {
				binding.activate();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.BasicFieldBinding#deactivate()
	 */
	@Override
	public void deactivate() {
		if(this.bindings != null){
			for (IFieldBinding binding : this.bindings) {
				binding.deactivate();
			}
		}
		super.deactivate();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.BasicFieldBinding#destroy()
	 */
	@Override
	public void destroy() {
		if(this.bindings != null){
			for (IFieldBinding binding : this.bindings) {
				binding.destroy();
			}
			this.bindings.clear();
			this.bindings = null;
		}
		super.destroy();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IFieldBinding#handleDataChanged(com.wxxr.mobile.core.ui.api.DataChangedEvent)
	 */
	@Override
	public void handleDataChanged(DataChangedEvent event) {
		IUIComponent source = event.getComponent();
		if(source == getField()){
			updateUI(false);
		}else if(source.isSubsidiaryOf(getField())&&(this.bindings != null)){
			for (IFieldBinding binding : this.bindings) {
				binding.handleDataChanged(event);
			}
		}
		
	}
}
