/**
 * 
 */
package com.wxxr.mobile.core.tools.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author neillin
 *
 */
public class NavigationModel {
	private String result, toPage, toView, message;
	private Map<String, Parameter> params;
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @return the toPage
	 */
	public String getToPage() {
		return toPage;
	}
	/**
	 * @return the toView
	 */
	public String getToView() {
		return toView;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @return the params
	 */
	public List<Parameter> getParams() {
		return params != null ? new ArrayList<Parameter>(params.values()) : null;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @param toPage the toPage to set
	 */
	public void setToPage(String toPage) {
		this.toPage = toPage;
	}
	/**
	 * @param toView the toView to set
	 */
	public void setToView(String toView) {
		this.toView = toView;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void addParameter(Parameter p){
		if(this.params == null){
			this.params = new HashMap<String, Parameter>();
		}
		this.params.put(p.getName(), p);
	}
}
