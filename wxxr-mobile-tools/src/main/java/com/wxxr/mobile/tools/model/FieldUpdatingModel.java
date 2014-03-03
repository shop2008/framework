/**
 * 
 */
package com.wxxr.mobile.tools.model;

/**
 * @author neillin
 *
 */
public class FieldUpdatingModel {
	private String[] fieldNames;
	private String message;
	/**
	 * @return the fieldNames
	 */
	public String[] getFieldNames() {
		return fieldNames;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param fieldNames the fieldNames to set
	 */
	public void setFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getFieldsString() {
		if((this.fieldNames != null)&&(this.fieldNames.length > 0)){
			StringBuffer buf = new StringBuffer();
			for (int i=0 ; i < this.fieldNames.length ; i++) {
				if(i > 0){
					buf.append(',');
				}
				buf.append('"').append(this.fieldNames[i]).append('"');
			}
			return buf.toString();
		}
		return null;

	}
}
