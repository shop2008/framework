/**
 * 
 */
package com.wxxr.mobile.core.model;

/**
 * @author neillin
 *
 */
public class BeanField extends FieldModel {
	private String beanType;
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
	public String getBeanType() {
		return beanType;
	}

	/**
	 * @param beanType the beanType to set
	 */
	public void setBeanType(String beanType) {
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
