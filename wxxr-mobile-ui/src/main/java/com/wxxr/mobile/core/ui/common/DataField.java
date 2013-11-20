/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import static com.wxxr.mobile.core.ui.common.ModelUtils.*;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IDomainValueModel;
import com.wxxr.mobile.core.ui.api.IFieldBinding;
import com.wxxr.mobile.core.ui.api.IReadable;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IUIContainer;
import com.wxxr.mobile.core.ui.api.IValidationErrorHandler;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.IWritable;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValidationError;

/**
 * @author neillin
 *
 */
public class DataField<T> extends UIComponent implements IDataField<T> {

	public final static String UPDATE_VALUE_COMMAND_NAME = "_updateValue";

	private AttributeKey<T> valueKey;
	private T localValue;
	private IDomainValueModel<T> domainModel;
	
	private boolean readOnly = false;
	
	private Class<T> valueType;
	
	private String valueFormat;

	private IReadable readable = new IReadable() {
		
		public T getValue() {
			return DataField.this.getValue();
		}
		
	};
	
	private IWritable writable = new IWritable() {
		
		public void setValue(Object value) {
			localValue = getValueKey().getValueType().cast(value);
			removeAttribute(AttributeKeys.validationErrors);
			setAttribute(valueKey, localValue);
			if(domainModel != null){
				// call domain model binding to update domain model
					ValidationError[] errs = domainModel.updateValue(value);
				if((errs != null)&&(errs.length > 0)){
					setAttribute(AttributeKeys.validationErrors, errs);
				}
			}
		}
		
		public ValidationError[] getValidationErrors() {
			return getAttribute(AttributeKeys.validationErrors);
		}
		
	};
		
	
	public DataField() {
		super();
	}


	public DataField(String name) {
		super(name);
	}	
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IAdaptable#getAdaptor(java.lang.Class)
	 */
	public <C> C getAdaptor(Class<C> clazz) {
		if(clazz == IReadable.class){
			return clazz.cast(readable);
		}
		if((clazz == IWritable.class)&&(!isReadOnly())){
			return clazz.cast(writable);
		}
		return super.getAdaptor(clazz);
	}


	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly() {
		return ((this.domainModel != null)&&(this.domainModel.isUpdatable() == false))||readOnly;
	}


	/**
	 * @return the valueType
	 */
	public Class<T> getValueType() {
		return valueType;
	}


	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * @param valueType the valueType to set
	 */
	public void setValueType(Class<T> valueType) {
		this.valueType = valueType;
	}


	/**
	 * @param objectValue the objectValue to set
	 */
	public void setObjectValue(T val) {
		setAttribute(getValueKey(), val);
	}


	public ValidationError[] getValidationErrors() {
		return getAttribute(AttributeKeys.validationErrors);
	}


	@SuppressWarnings("unchecked")
	public T getValue() {
		if(this.domainModel != null){
			return (T)this.domainModel.getValue();
		}else{
			return getAttribute(getValueKey());
		}
	}


	/**
	 * @return the valueFormat
	 */
	public String getValueFormat() {
		return valueFormat;
	}


	/**
	 * @param valueFormat the valueFormat to set
	 */
	public void setValueFormat(String valueFormat) {
		this.valueFormat = valueFormat;
	}


	/**
	 * @return the valueKey
	 */
	public AttributeKey<T> getValueKey() {
		return valueKey;
	}


	/**
	 * @param valueKey the valueKey to set
	 */
	public void setValueKey(AttributeKey<T> valueKey) {
		this.valueKey = valueKey;
	}

	protected T getLocalValue() {
		return this.localValue;
	}


	/**
	 * @return the domainModel
	 */
	public IDomainValueModel<T> getDomainModel() {
		return domainModel;
	}


	/**
	 * @param domainModel the domainModel to set
	 */
	public void setDomainModel(IDomainValueModel<T> domainModel) {
		this.domainModel = domainModel;
	}


	@Override
	public void setValue(T val) {
		setAttribute(valueKey, val);
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.UIComponent#invokeCommand(java.lang.String, com.wxxr.mobile.core.ui.api.InputEvent)
	 */
	@Override
	public void invokeCommand(String cmdName, InputEvent event) {
		if(UPDATE_VALUE_COMMAND_NAME.equals(cmdName)){
			IFieldBinding binding = getFieldBinding(this);
			if(binding != null){
				binding.updateModel();
			}
		}
		super.invokeCommand(cmdName, event);
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.UIComponent#init(com.wxxr.mobile.core.ui.api.IWorkbenchRTContext)
	 */
	@Override
	public void init(IWorkbenchRTContext ctx) {
		if(valueType == null){
			throw new IllegalStateException("Value type of data field was not setup correctly !");
		}
		if(valueKey == null){
			valueKey = new AttributeKey<T>(valueType, "value");
		}
		super.init(ctx);
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.UIComponent#setParent(com.wxxr.mobile.core.ui.api.IUIContainer)
	 */
	@Override
	public UIComponent setParent(IUIContainer<IUIComponent> parent) {
		super.setParent(parent);
		if((parent instanceof ViewBase)&&(this.domainModel != null)){
			((ViewBase)parent).registerDomainModel(this.domainModel);
		}
		return this;
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.UIComponent#destroy()
	 */
	@Override
	public void destroy() {
		if((getParent() instanceof ViewBase)&&(this.domainModel != null)){
			((ViewBase)getParent()).unregisterDomainModel(this.domainModel);
		}
		super.destroy();
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataField:[" + getName() + "]";
	}
	
}
