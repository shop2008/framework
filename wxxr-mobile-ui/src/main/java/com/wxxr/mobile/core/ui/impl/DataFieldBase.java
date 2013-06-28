/**
 * 
 */
package com.wxxr.mobile.core.ui.impl;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.AttributeKeys;
import com.wxxr.mobile.core.ui.api.ErrorCodes;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IReadable;
import com.wxxr.mobile.core.ui.api.IUIManagementContext;
import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IValueConvertorContext;
import com.wxxr.mobile.core.ui.api.IWritable;
import com.wxxr.mobile.core.ui.api.UIError;
import com.wxxr.mobile.core.ui.api.ValidationException;
import com.wxxr.mobile.core.ui.utils.ConvertorRegistry;

/**
 * @author neillin
 *
 */
public class DataFieldBase<T> extends AbstractUIComponent implements IDataField<T> {

	private AttributeKey<T> value = new AttributeKey<T>() {

		public Class<T> getValueType() {
			return valueType;
		}

		public IValueConvertor<T> getValueConvertor() {
			return getConvertor();
		}

		public String getName() {
			return "value";
		}
	};
	
	private boolean readOnly = false;
	private IValueConvertor<T> convertor;
	
	private Class<T> valueType;
	
	private String stringValue, valueFormat;

	private IReadable readable = new IReadable() {
		
		public T getValue() {
			return DataFieldBase.this.getValue();
		}
		
		public String getStringValue() {
			return stringValue;
		}
	};
	
	private IWritable writable = new IWritable() {
		
		public void setStringValue(String value) {
			stringValue = value;
			doValidate();
		}
		
		public UIError getValidationError() {
			return uiError;
		}
		
	};
	
	private UIError uiError;
	
	
	public boolean isFieldRequired() {
		Boolean val = getAttribute(AttributeKeys.required);
		return val != null ? val.booleanValue() : false;
	}

	
	protected void doValidate() {
		String val = stringValue;
		if(isFieldRequired()&&(val == null)){
			uiError = new UIError(ErrorCodes.FIELD_REQUIRED, "input_required_value", getName());
			setAttribute(value, (T)null);
			return;
		}else if(val != null){
			try {
				setValue(val);
			} catch (ValidationException e) {
				uiError = new UIError(ErrorCodes.FIELD_REQUIRED, e.getMessage(), getName());
				setAttribute(value, (T)null);
				return;
			}
		}else{
			setAttribute(value, (T)null);
		}
		uiError = null;
	}
	

	protected void setValue(String val) throws ValidationException {
		setAttribute(value, getConvertor().valueOf(val, new IValueConvertorContext() {
			
			public IUIManagementContext getManagementContext() {
				return getUIContext();
			}
			
			public String getFormat() {
				return getValueFormat();
			}
		}));
	}
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
		if((clazz == IWritable.class)&&( readOnly == false)){
			return clazz.cast(writable);
		}
		return super.getAdaptor(clazz);
	}


	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly() {
		return readOnly;
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
		setAttribute(value, val);
	}


	public UIError getUIError() {
		return this.uiError;
	}


	public T getValue() {
		return getAttribute(value);
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

	
}
