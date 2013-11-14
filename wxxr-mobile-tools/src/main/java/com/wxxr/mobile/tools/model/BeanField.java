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
	
	
	

}
