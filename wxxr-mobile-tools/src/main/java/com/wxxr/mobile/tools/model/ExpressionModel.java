/**
 * 
 */
package com.wxxr.mobile.tools.model;

/**
 * @author neillin
 *
 */
public class ExpressionModel {
	private String expression;
	private JavaModel field;
	private String attributeKey;
	
	/**
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}
	/**
	 * @return the field
	 */
	public JavaModel getField() {
		return field;
	}
	/**
	 * @return the attributeKey
	 */
	public String getAttributeKey() {
		return attributeKey;
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
	public void setField(JavaModel field) {
		this.field = field;
	}
	/**
	 * @param attributeKey the attributeKey to set
	 */
	public void setAttributeKey(String attributeKey) {
		this.attributeKey = attributeKey;
	}
	
	public String getKey() {
		return field.getName()+"."+attributeKey;
	}
	
	public boolean isElexpress() {
		return (this.expression.startsWith("${")||this.expression.startsWith("#{"))&&this.expression.endsWith("}");
	}
}
