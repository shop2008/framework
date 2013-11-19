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
	
	public String getStringValue() {
		if(this.value == null){
			return "null";
		}
		switch(this.type){
		case A_BOOLEAN:
			return "new boolean[]{"+this.value+"}";
		case A_CHAR:
			return "new char[]{"+this.value+"}";
		case A_BYTE:
			return "new byte[]{"+this.value+"}";
		case A_DOUBLE:
			return "new double[]{"+this.value+"}";
		case A_FLOAT:
			return "new float[]{"+this.value+"}";
		case A_INETGER:
			return "new int[]{"+this.value+"}";
		case A_LONG:
			return "new long[]{"+this.value+"}";
		case A_SHORT:
			return "new short[]{"+this.value+"}";
		case A_STRING:
			return "new String[]{\""+this.value+"\"}";
		case BOOLEAN:
			return this.value;
		case BYTE:
			return "(byte)"+this.value;
		case CHAR:
			return "(char)"+this.value;
		case DOUBLE:
			return "(double)"+this.value;
		case FLOAT:
			return "(float)"+this.value;
		case INETGER:
			return this.value;
		case LONG:
			return "(long)"+this.value;
		case SHORT:
			return "(short)"+this.value;
		case STRING:
			return "\""+this.value+"\"";
		}
		return "";
	}
	
}
