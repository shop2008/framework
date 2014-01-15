/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public class ValidationError {
	private final String errorCode, errorMessage,fieldId;
	
	public ValidationError(String errCode, String errMsg, String fieldId){
		this.errorCode = errCode;
		this.errorMessage = errMsg;
		this.fieldId = fieldId;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @return the fieldId
	 */
	public String getFieldId() {
		return fieldId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ValidationError [errorCode=" + errorCode + ", errorMessage="
				+ errorMessage + ", fieldId=" + fieldId + "]";
	}
	
	

}
