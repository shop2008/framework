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
	private boolean updateAsync;
	
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
	 * @return the updateAsync
	 */
	public boolean isUpdateAsync() {
		return updateAsync;
	}
	/**
	 * @param updateAsync the updateAsync to set
	 */
	public void setUpdateAsync(boolean updateAsync) {
		this.updateAsync = updateAsync;
	}
}
