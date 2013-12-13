/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;



import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IBindingDecoratorRegistry;
import com.wxxr.mobile.core.ui.api.IBindingValueChangedCallback;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;
import com.wxxr.mobile.core.ui.api.IFieldBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IUIDecorator;
import com.wxxr.mobile.core.ui.api.IUIUpdatingContext;
import com.wxxr.mobile.core.ui.api.IValidationErrorHandler;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValidationError;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;

/**
 * @author neillin
 *
 */
public abstract class AbstractFieldBinding implements IFieldBinding {

	private Object uiControl;	// physical component
	private IView viewModel;	// Logic view component
	private IWorkbenchRTContext workbenchContext;
	private IBindingContext bindingContext;
	private Map<String, String> bindingAttrs;
	private String fieldName;
	protected boolean updateUIScheduled,active;
	private IUIComponent field;
	private IUIDecorator decorator;
	private IUIUpdatingContext updatingCtx = new IUIUpdatingContext() {
		
		@Override
		public IWorkbenchRTContext getWorkbenchContext() {
			return workbenchContext;
		}
		
		@Override
		public IView getView() {
			return viewModel;
		}
		
		@Override
		public IBindingContext getBindingContext() {
			return bindingContext;
		}
	};
	protected Map<String, String> getBindingAttrs(boolean createIfNotExisting){
		if((bindingAttrs == null)&&createIfNotExisting){
			this.bindingAttrs = new HashMap<String, String>();
		}
		return this.bindingAttrs;
	}
	
	protected synchronized void scheduleUIUpdating() {
		if(updateUIScheduled == false){
			updateUIScheduled = true;
			KUtils.runOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					doUIUpdating();
				}
			}, 100, TimeUnit.MILLISECONDS);
		}
	}
	
	protected synchronized void doUIUpdating() {
		this.decorator.handleUIUpdating(updatingCtx, field, uiControl);
		updateUIScheduled = false;
	}
	
	private void doUpdateUI(final boolean recursive){
		KUtils.runOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				decorator.handleUIUpdating(updatingCtx, field, uiControl);
			}
		});
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IAndroidBinding#updateUI()
	 */
	protected void updateUI(boolean recursive) {
		if(!active){
			return;
		}
		IUIComponent field = this.viewModel.getChild(getFieldName());
		Set<AttributeKey<?>> keys = field.getAttributeKeys();
		if((keys != null)&&(keys.size() > 0)){
			for (AttributeKey<?> attrKey : keys) {
				updateControl(attrKey,field.getAttribute(attrKey));
			}
		}
	}

	/**
	 * @param attrMgr
	 * @param attrKey
	 */
	protected void updateControl(AttributeKey<?> attrKey,Object val) {
		IFieldAttributeManager attrMgr = workbenchContext.getWorkbenchManager().getFieldAttributeManager();
		@SuppressWarnings("unchecked")
		IAttributeUpdater<Object>[] updaters = (IAttributeUpdater<Object>[])attrMgr.getAttributeUpdaters(attrKey.getName());
		if(updaters != null){
			for (IAttributeUpdater<Object> updater : updaters) {
				if(updater.acceptable(this.uiControl)){
					updater.updateControl(this.uiControl, attrKey, this.viewModel.getChild(getFieldName()),val);
					break;
				}
			}
		}
	}
	
	
	
	protected boolean isVisible() {
		Boolean bool = this.viewModel.getChild(getFieldName()).getAttribute(AttributeKeys.visible);
		return bool != null ? bool.booleanValue() : false;
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
				public void valueChanged(final UIComponent component, final AttributeKey<?>... keys) {
					scheduleUIUpdating();
				}
			});
		}
		this.active = true;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IAndroidBinding#deactivate()
	 */
	@Override
	public void deactivate() {
		this.active = false;
		if(this.field != null){
			this.field.setValueChangedCallback(null);
			this.field = null;
		}
		if(this.viewModel != null){
			this.viewModel = null;
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IAndroidBinding#destroy()
	 */
	@Override
	public void destroy() {
		this.uiControl = null;
		this.viewModel = null;
		this.workbenchContext = null;
		this.bindingAttrs = null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBinding#isInitialized()
	 */
	@Override
	public boolean isInitialized() {
		return this.workbenchContext != null;
	}


	/**
	 * @return the field
	 */
	protected IUIComponent getField() {
		return this.viewModel != null ? this.viewModel.getChild(getFieldName()) : null;
	}

	@Override
	public void init(IWorkbenchRTContext ctx) {
		this.workbenchContext = ctx;
		this.decorator = new IUIDecorator() {
			
			@Override
			public void handleUIUpdating(IUIUpdatingContext context, IUIComponent comp,
					Object uiControl) {
				updateUI(true);
			}
		};
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
		return this.uiControl;
	}

	/**
	 * @return the bContext
	 */
	protected IWorkbenchRTContext getWorkbenchContext() {
		return this.workbenchContext;
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


	@Override
	public void updateModel() {
		final IUIComponent field = getField();
		if(field instanceof IDataField){
			AttributeKey<?> attrKey =((IDataField<?>)field).getValueKey();
			IFieldAttributeManager attrMgr = workbenchContext.getWorkbenchManager().getFieldAttributeManager();
			@SuppressWarnings("unchecked")
			IAttributeUpdater<Object>[] updaters = (IAttributeUpdater<Object>[])attrMgr.getAttributeUpdaters(attrKey.getName());
			if(updaters != null){
				for (IAttributeUpdater<Object> updater : updaters) {
					if(updater.acceptable(this.uiControl)){
						updater.updateModel(this.uiControl, attrKey, field);
						final ValidationError[] errors = ((IDataField<?>) field).getValidationErrors();
						if((errors != null)&&(errors.length > 0)){
							final IValidationErrorHandler handler = workbenchContext.getWorkbenchManager().getValidationErrorHandler();
							if(handler != null){
								KUtils.runOnUIThread(new Runnable() {
									@Override
									public void run() {
										handler.handleValidationError(field,uiControl,errors);
									}
								},0,null);
							}
						}
						break;
					}
				}
			}
		}
		
	}

	@Override
	public void doUpdate() {
		doUpdateUI(true);
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IDecoratable#doDecorate(java.lang.String[])
	 */
	@Override
	public IUIDecorator doDecorate(String... decoratorNames) {
		IBindingDecoratorRegistry registry = this.workbenchContext.getWorkbenchManager().getBindingDecoratorRegistry();
		if((decoratorNames != null)&&(decoratorNames.length > 0)){
			for (String name : decoratorNames) {
				this.decorator = registry.createDecorator(name, this.decorator);
			}
		}
		return this.decorator;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IDecoratable#getDecoratorClass()
	 */
	@Override
	public Class<IUIDecorator> getDecoratorClass() {
		return IUIDecorator.class;
	}



	/**
	 * @return the viewModel
	 */
	protected IView getViewModel() {
		return viewModel;
	}


	/**
	 * @return the bindingContext
	 */
	protected IBindingContext getBindingContext() {
		return bindingContext;
	}


	/**
	 * @param uiControl the uiControl to set
	 */
	protected void setUIControl(Object uiControl) {
		this.uiControl = uiControl;
	}

	/**
	 * @param viewModel the viewModel to set
	 */
	protected void setViewModel(IView viewModel) {
		this.viewModel = viewModel;
	}

	/**
	 * @param workbenchContext the workbenchContext to set
	 */
	protected void setWorkbenchContext(IWorkbenchRTContext workbenchContext) {
		this.workbenchContext = workbenchContext;
	}

	/**
	 * @param bindingContext the bindingContext to set
	 */
	protected void setBindingContext(IBindingContext bindingContext) {
		this.bindingContext = bindingContext;
	}

	/**
	 * @param bindingAttrs the bindingAttrs to set
	 */
	protected void setBindingAttrs(Map<String, String> bindingAttrs) {
		this.bindingAttrs = bindingAttrs;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	protected void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

}
