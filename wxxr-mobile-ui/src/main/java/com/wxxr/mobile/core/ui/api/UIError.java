/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public class UIError {
	private final String errorCode, errorMessage,fieldId;
	
	public UIError(String errCode, String errMsg, String fieldId){
		this.errorCode = errCode;
		this.errorMessage = errMsg;
		this.fieldId = fieldId;
	}

	/**
	 * @return the errorCode
	 */
	protected String getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	protected String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @return the fieldId
	 */
	protected String getFieldId() {
		return fieldId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UIError [errorCode=" + errorCode + ", errorMessage="
				+ errorMessage + ", fieldId=" + fieldId + "]";
	}
	
	

}
