/**
 * 
 */
package com.wxxr.mobile.core.ui.api;


/**
 * @author neillin
 *
 */
public class CommandResult {
	private String result;
	private Object payload;
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @return the payload
	 */
	public Object getPayload() {
		return payload;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @param payload the payload to set
	 */
	public void setPayload(Object payload) {
		this.payload = payload;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CommandResult [result=" + result + ", payload=" + payload + "]";
	}
	
}
