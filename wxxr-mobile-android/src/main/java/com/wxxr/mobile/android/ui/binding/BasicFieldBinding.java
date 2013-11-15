/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.view.View;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IBindingValueChangedCallback;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;
import com.wxxr.mobile.core.ui.api.IFieldBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.UIComponent;

/**
 * @author neillin
 *
 */
public class BasicFieldBinding implements IFieldBinding {
	
	protected View pComponent;	// physical component
	protected IView viewModel;	// Logic view component
	protected IWorkbenchRTContext bContext;
	protected IAndroidBindingContext context;
	protected Map<String, String> bindingAttrs;
	protected String fieldName;
	protected IUIComponent field;
	
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
	
	private void doUpdateUI(final boolean recursive){
		AppUtils.runOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				updateUI(recursive);
			}
		});
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IAndroidBinding#updateUI()
	 */
	protected void updateUI(boolean recursive) {
		IUIComponent field = this.viewModel.getChild(getFieldName());
		Set<AttributeKey<?>> keys = field.getAttributeKeys();
		if((keys != null)&&(keys.size() > 0)){
			for (AttributeKey<?> attrKey : keys) {
				updateControl(attrKey,field.getAttribute(attrKey));
			}
//			if(field instanceof IDataField<?>){
//				AttributeKey<?> attrKey = ((IDataField<?>)field).getValueKey();
//				updateControl(attrKey,((IDataField<?>)field).getValue());
//			}
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
					updater.updateControl(this.pComponent, attrKey, this.viewModel.getChild(getFieldName()),val);
					break;
				}
			}
		}
	}
	
	
	
	protected boolean isVisible() {
		Boolean bool = this.viewModel.getChild(getFieldName()).getAttribute(AttributeKeys.visible);
		return bool != null ? bool.booleanValue() : false;
	}

	/**
	 * 
	 */
	protected void setUIEnabled() {
		Boolean bool = this.viewModel.getChild(getFieldName()).getAttribute(AttributeKeys.enabled);
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
	public void activate(IView model) {
		this.viewModel = model;
		this.field = this.viewModel.getChild(getFieldName());
		if(this.field != null){
			this.field.setValueChangedCallback(new IBindingValueChangedCallback() {
				
				@Override
				public void valueChanged(UIComponent component, AttributeKey<?>... keys) {
					handleValueChangedCallback(component, keys);
				}
			});
		}
		doUpdateUI(true);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IAndroidBinding#deactivate()
	 */
	@Override
	public void deactivate() {
		if(this.viewModel != null){
			this.viewModel = null;
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IAndroidBinding#destroy()
	 */
	@Override
	public void destroy() {
		this.pComponent = null;
		this.viewModel = null;
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
		return this.viewModel != null ? this.viewModel.getChild(getFieldName()) : null;
	}

	@Override
	public void init(IWorkbenchRTContext ctx) {
		this.bContext = ctx;
	}

	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
		for (ValueChangedEvent event : events) {
			if(event.getSource() == getField()){
				doUpdateUI(true);
				break;
			}
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


	@Override
	public void updateModel() {
		IUIComponent field = getField();
		if(field instanceof IDataField){
			AttributeKey<?> attrKey =((IDataField<?>)field).getValueKey();
			IFieldAttributeManager attrMgr = bContext.getWorkbenchManager().getFieldAttributeManager();
			@SuppressWarnings("unchecked")
			IAttributeUpdater<View>[] updaters = (IAttributeUpdater<View>[])attrMgr.getAttributeUpdaters(attrKey.getName());
			if(updaters != null){
				for (IAttributeUpdater<View> updater : updaters) {
					if(updater.acceptable(pComponent)){
						updater.updateModel(this.pComponent, attrKey, field);
						break;
					}
				}
			}
		}
		
	}

	@Override
	public void refresh() {
		doUpdateUI(true);
	}

	/**
	 * 
	 */
	protected void handleValueChangedCallback(UIComponent component, AttributeKey<?>... keys) {
		// doUpdateUI(true);
	}


}
