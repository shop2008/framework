/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.view.View;

import com.wxxr.mobile.android.ui.IAndroidBinding;
import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;

/**
 * @author neillin
 *
 */
public class BasicFieldBinding implements IAndroidBinding<IUIComponent> {
	
	private View pComponent;	// physical component
	private IUIComponent field;	// Logic view component
	private IWorkbenchRTContext bContext;
	private IAndroidBindingContext context;
	private Map<String, String> bindingAttrs;
	private String fieldName;
	
	public BasicFieldBinding(IAndroidBindingContext ctx, String fieldName,Map<String,String> attrSet){
		this.pComponent = ctx.getBindingControl();
		this.fieldName = fieldName;
		this.bindingAttrs = attrSet;
		this.context = ctx;
	}
	
	protected Map<String, String> getBindingAttrs(boolean createIfNotExisting){
		if((bindingAttrs == null)&&createIfNotExisting){
			this.bindingAttrs = new HashMap<String, String>();
		}
		return this.bindingAttrs;
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IAndroidBinding#updateUI()
	 */
	protected void updateUI(boolean recursive) {
		Set<AttributeKey<?>> keys = field.getAttributeKeys();
		if((keys != null)&&(keys.size() > 0)){
			for (AttributeKey<?> attrKey : keys) {
				updateControl(attrKey,field.getAttribute(attrKey));
			}
			if(field instanceof IDataField<?>){
				AttributeKey<?> attrKey = ((IDataField<?>)field).getValueKey();
				updateControl(attrKey,((IDataField<?>)field).getValue());
			}
		}
	}

	/**
	 * @param attrMgr
	 * @param attrKey
	 */
	protected void updateControl(AttributeKey<?> attrKey,Object val) {
		IFieldAttributeManager attrMgr = bContext.getWorkbenchManager().getFieldAttributeManager();
		@SuppressWarnings("unchecked")
		IAttributeUpdater<View>[] updaters = (IAttributeUpdater<View>[])attrMgr.getAttributeUpdaters(attrKey.getName());
		if(updaters != null){
			for (IAttributeUpdater<View> updater : updaters) {
				if(updater.acceptable(pComponent)){
					updater.updateControl(this.pComponent, attrKey, field,val);
					break;
				}
			}
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
	

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IAndroidBinding#activate()
	 */
	@Override
	public void activate(IUIComponent model) {
		this.field = model;
//		if(this.field != null){
//			this.field.doBinding(this);
//		}
		updateUI(true);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IAndroidBinding#deactivate()
	 */
	@Override
	public void deactivate() {
		if(this.field != null){
//			this.field.doUnbinding(this);
			this.field = null;
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IAndroidBinding#destroy()
	 */
	@Override
	public void destroy() {
		this.pComponent = null;
		this.field = null;
		this.bContext = null;
		this.bindingAttrs = null;
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
	public void init(IWorkbenchRTContext ctx) {
		this.bContext = ctx;
	}

	@Override
	public void notifyDataChanged(ValueChangedEvent event) {
		if(event.getComponent() == getField()){
			updateUI(true);
		}
	}

	@Override
	public Object getUIControl() {
		return pComponent;
	}

	/**
	 * @return the bContext
	 */
	protected IWorkbenchRTContext getWorkbenchContext() {
		return bContext;
	}

	/**
	 * @return the bindingAttrs
	 */
	protected Map<String, String> getBindingAttrs() {
		return bindingAttrs;
	}

	/**
	 * @return the fieldName
	 */
	protected String getFieldName() {
		return fieldName;
	}

	/**
	 * @return the context
	 */
	protected IAndroidBindingContext getAndroidBindingContext() {
		return context;
	}


}
