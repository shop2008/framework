/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IDomainValueModel;
import com.wxxr.mobile.core.ui.api.IReadable;
import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IWritable;
import com.wxxr.mobile.core.ui.api.UIError;
import com.wxxr.mobile.core.ui.api.ValidationException;
import com.wxxr.mobile.core.ui.utils.ConvertorRegistry;

/**
 * @author neillin
 *
 */
public class DataField<T> extends UIComponent implements IDataField<T> {

	private AttributeKey<T> valueKey;
	private T localValue;
	private IDomainValueModel domainModel;
//	= new AttributeKey<T>() {
//
//		public Class<T> getValueType() {
//			return valueType;
//		}
//
//		public IValueConvertor<T> getValueConvertor() {
//			return getConvertor();
//		}
//
//		public String getName() {
//			return "value";
//		}
//	};
	
	private boolean readOnly = false;
	private IValueConvertor<T> convertor;
	
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
			if(domainModel != null){
				// call domain model binding to update domain model
				try {
					domainModel.updateValue(value);
				} catch (ValidationException e) {
					uiError = new UIError(e.getErrorCode(), e.getMessage(), getName());
				}
			}
//			stringValue = value;
//			doValidate();
		}
		
		public UIError getValidationError() {
			return uiError;
		}
		
	};
	
	private UIError uiError;
	
	
//	public boolean isFieldRequired() {
//		Boolean val = getAttribute(AttributeKeys.required);
//		return val != null ? val.booleanValue() : false;
//	}

	
//	protected void doValidate() {
//		String val = stringValue;
//		if(isFieldRequired()&&(val == null)){
//			uiError = new UIError(ErrorCodes.FIELD_REQUIRED, "input_required_value", getName());
//			setAttribute(value, (T)null);
//			return;
//		}else if(val != null){
//			try {
//				setValue(val);
//			} catch (ValidationException e) {
//				uiError = new UIError(ErrorCodes.FIELD_REQUIRED, e.getMessage(), getName());
//				setAttribute(value, (T)null);
//				return;
//			}
//		}else{
//			setAttribute(value, (T)null);
//		}
//		uiError = null;
//	}
	

//	protected void setValue(String val) throws ValidationException {
//		setAttribute(value, getConvertor().valueOf(val, new IValueConvertorContext() {
//			
//			public IWorkbenchRTContext getManagementContext() {
//				return getUIContext();
//			}
//			
//			public String getFormat() {
//				return getValueFormat();
//			}
//		}));
//	}
	/**
	 * @return
	 */
	protected IValueConvertor<T> getConvertor() {
		IValueConvertor<T> v = convertor;
		if(v == null){
			v = ConvertorRegistry.getInstance().getConverter(getValueType());
		}
		return v;
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
	 * @param convertor the convertor to set
	 */
	public void setConvertor(IValueConvertor<T> validator) {
		this.convertor = validator;
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


	public UIError getUIError() {
		return this.uiError;
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
	public IDomainValueModel getDomainModel() {
		return domainModel;
	}


	/**
	 * @param domainModel the domainModel to set
	 */
	public void setDomainModel(IDomainValueModel domainModel) {
		this.domainModel = domainModel;
	}


	@Override
	public void setValue(T val) {
		setAttribute(valueKey, val);
	}
	
}
