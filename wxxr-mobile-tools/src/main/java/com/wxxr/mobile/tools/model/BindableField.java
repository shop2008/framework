/**
 * 
 */
package com.wxxr.mobile.tools.model;

/**
 * @author neillin
 *
 */
public class BindableField extends FieldModel {
	private String decoratedType;
	/**
	 * @return the decorated
	 */
	public boolean isDecorated() {
		return this.decoratedType != null;
	}
	/**
	 * @return the decoratedType
	 */
	public String getDecoratedType() {
		return decoratedType;
	}

	/**
	 * @param decoratedType the decoratedType to set
	 */
	public void setDecoratedType(String decoratedType) {
		this.decoratedType = decoratedType;
	}
	
}
