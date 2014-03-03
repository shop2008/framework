/**
 * 
 */
package com.wxxr.mobile.tools.model;

import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;

/**
 * @author neillin
 *
 */
public class BeanField extends FieldModel {
	private BindingType beanType;
	private String valueExpression;
	private boolean nullable;
	private String[] efftectingFields;
	
	/**
	 * @return the valueExpression
	 */
	public String getValueExpression() {
		return valueExpression;
	}

	/**
	 * @param valueExpression the valueExpression to set
	 */
	public void setValueExpression(String valueExpression) {
		this.valueExpression = valueExpression;
	}

	/**
	 * @return the beanType
	 */
	public BindingType getBeanType() {
		return beanType;
	}

	public boolean isService() {
		return this.beanType == BindingType.Service;
	}
	
	/**
	 * @param beanType the beanType to set
	 */
	public void setBeanType(BindingType beanType) {
		this.beanType = beanType;
	}

	/**
	 * @return the nullable
	 */
	public boolean isNullable() {
		return nullable;
	}

	/**
	 * @param nullable the nullable to set
	 */
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	/**
	 * @return the efftectingFields
	 */
	public String[] getEfftectingFields() {
		return efftectingFields;
	}

	/**
	 * @param efftectingFields the efftectingFields to set
	 */
	public void setEfftectingFields(String[] efftectingFields) {
		this.efftectingFields = efftectingFields;
	}
	
	public String getEfftectingFieldsString() {
		if((this.efftectingFields != null)&&(this.efftectingFields.length > 0)){
			StringBuffer buf = new StringBuffer();
			for (int i=0 ; i < this.efftectingFields.length ; i++) {
				if(i > 0){
					buf.append(',');
				}
				buf.append('"').append(this.efftectingFields[i]).append('"');
			}
			return buf.toString();
		}
		return null;
	}
	

}
