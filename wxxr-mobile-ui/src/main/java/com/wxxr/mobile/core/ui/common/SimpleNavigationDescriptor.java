/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.core.ui.api.INavigationDescriptor;

/**
 * @author neillin
 *
 */
public class SimpleNavigationDescriptor implements INavigationDescriptor {
	
	private String result,message,toView,toPage;
	private Map<String, String> params;


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.INavigationDescriptor#getParameters()
	 */
	public Map<String, String> getParameters() {
		return this.params != null ? Collections.unmodifiableMap(this.params) : null;
	}


	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}


	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}


	/**
	 * @return the toView
	 */
	public String getToView() {
		return toView;
	}


	/**
	 * @return the toPage
	 */
	public String getToPage() {
		return toPage;
	}


	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}


	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * @param toView the toView to set
	 */
	public void setToView(String toView) {
		this.toView = toView;
	}


	/**
	 * @param toPage the toPage to set
	 */
	public void setToPage(String toPage) {
		this.toPage = toPage;
	}
	
	public SimpleNavigationDescriptor addParameter(String name, String value){
		if(this.params == null){
			this.params = new HashMap<String, String>();
		}
		this.params.put(name, value);
		return this;
	}
	
	public SimpleNavigationDescriptor removeParameter(String name){
		if(this.params != null){
			this.params.remove(name);
		}
		return this;
	}

}
