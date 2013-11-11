/**
 * 
 */
package com.wxxr.mobile.tools.model;

/**
 * @author neillin
 *
 */
public class Parameter {
	
	private String name, value;

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
	
}
