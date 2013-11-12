/**
 * 
 */
package com.wxxr.mobile.tools.model;

import com.wxxr.mobile.core.ui.annotation.ValueType;

/**
 * @author neillin
 *
 */
public class Parameter {
	
	private String name, value;
	private ValueType type;

	public Parameter(){
	}
	
	public Parameter(String name, String value){
		this.name = name;
		this.value = value;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the type
	 */
	public ValueType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ValueType type) {
		this.type = type;
	}
	
}
