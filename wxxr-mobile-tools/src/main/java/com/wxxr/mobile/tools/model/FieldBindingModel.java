/**
 * 
 */
package com.wxxr.mobile.tools.model;


/**
 * @author neillin
 *
 */
public class FieldBindingModel {
	private String expression;
	private String javaStatement;
	private boolean updatable;
	
	/**
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}
	/**
	 * @param expression the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
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
	 * @return the updatable
	 */
	public boolean isUpdatable() {
		return updatable;
	}
	/**
	 * @param updatable the updatable to set
	 */
	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}
}
