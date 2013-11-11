/**
 * 
 */
package com.wxxr.mobile.tools.model;

import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;

/**
 * @author neillin
 *
 */
public class BeanBindingModel {
	private String expression;
	private FieldModel field;
	private BindingType type;
	private String javaStatement;
	private boolean nullable;
	
	/**
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}
	/**
	 * @return the field
	 */
	public FieldModel getField() {
		return field;
	}
	
	/**
	 * @param expression the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	/**
	 * @param field the field to set
	 */
	public void setField(FieldModel field) {
		this.field = field;
	}
	/**
	 * @return the type
	 */
	public BindingType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(BindingType type) {
		this.type = type;
	}
	
	public String getKey() {
		return this.field.getName();
	}
	/**
	 * @return the javaStatement
	 */
	public String getJavaStatement() {
		return javaStatement;
	}
	/**
	 * @param javaStatement the javaStatement to set
	 */
	public void setJavaStatement(String javaStatement) {
		this.javaStatement = javaStatement;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BeanBindingModel [expression=" + expression + ", field="
				+ field.getName() + ", type=" + type + ", nullable=" + nullable + "]";
	}
}
