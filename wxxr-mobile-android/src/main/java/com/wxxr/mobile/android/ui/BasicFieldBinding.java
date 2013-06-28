/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.view.View;

import com.wxxr.mobile.core.ui.api.AttributeKeys;
import com.wxxr.mobile.core.ui.api.DataChangedEvent;
import com.wxxr.mobile.core.ui.api.IUIComponent;

/**
 * @author neillin
 *
 */
public class BasicFieldBinding implements IFieldBinding {
	
	private View pComponent;	// physical component
	private IUIComponent field;	// Logic view component
	private IBindingContext bContext;
	
	public BasicFieldBinding(View view, IUIComponent field){
		this.pComponent = view;
		this.field = field;
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IFieldBinding#updateUI()
	 */
	@Override
	public void updateUI(boolean recursive) {
		if(field.hasAttribute(AttributeKeys.enabled)){
			setUIEnabled();
		}
		if(field.hasAttribute(AttributeKeys.visible)){
			setUIVisibility();
		}
	}
	
	
	
	protected boolean isVisible() {
		Boolean bool = field.getAttribute(AttributeKeys.visible);
		return bool != null ? bool.booleanValue() : false;
	}

	/**
	 * 
	 */
	protected void setUIEnabled() {
		Boolean bool = field.getAttribute(AttributeKeys.enabled);
		boolean val = bool != null ? bool.booleanValue() : false;
		this.pComponent.setEnabled(val);
	}
	
	protected void setUIVisibility() {
		boolean val = isVisible();
		if(val){
			this.pComponent.setVisibility(View.VISIBLE);
		}else{
			this.pComponent.setVisibility(View.GONE);
		}
	}
	
	protected IBindingContext getBindingContext() {
		return this.bContext;
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IFieldBinding#activate()
	 */
	@Override
	public void activate() {

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IFieldBinding#deactivate()
	 */
	@Override
	public void deactivate() {
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IFieldBinding#destroy()
	 */
	@Override
	public void destroy() {
		this.pComponent = null;
		this.field = null;
		this.bContext = null;
	}

	/**
	 * @return the pComponent
	 */
	protected View getComponent() {
		return pComponent;
	}

	/**
	 * @return the field
	 */
	protected IUIComponent getField() {
		return field;
	}

	@Override
	public void init(IBindingContext ctx) {
		this.bContext = ctx;
	}

	@Override
	public void handleDataChanged(DataChangedEvent event) {
		if(event.getComponent() == getField()){
			updateUI(false);
		}
	}

}
