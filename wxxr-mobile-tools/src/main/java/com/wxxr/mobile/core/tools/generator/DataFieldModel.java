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
public class DataFieldModel extends FieldModel {
	private String enableWhenExpress;
	private String visibleWhenExpress;
	private Map<String, Parameter> attributes;
	private String valueKey;
	/**
	 * @return the enableWhenExpress
	 */
	public String getEnableWhenExpress() {
		return enableWhenExpress;
	}
	/**
	 * @return the visibleWhenExpress
	 */
	public String getVisibleWhenExpress() {
		return visibleWhenExpress;
	}
	/**
	 * @param enableWhenExpress the enableWhenExpress to set
	 */
	public void setEnableWhenExpress(String enableWhenExpress) {
		this.enableWhenExpress = enableWhenExpress;
	}
	/**
	 * @param visibleWhenExpress the visibleWhenExpress to set
	 */
	public void setVisibleWhenExpress(String visibleWhenExpress) {
		this.visibleWhenExpress = visibleWhenExpress;
	}
	
	/**
	 * @return the params
	 */
	public List<Parameter> getAttributes() {
		return this.attributes != null ? new ArrayList<Parameter>(attributes.values()) : null;
	}

	public void addAttribute(String key,String value){
		if(this.attributes == null){
			this.attributes = new HashMap<String, Parameter>();
		}
		Parameter p = new Parameter(key, value);
		this.attributes.put(p.getName(), p);
	}
	/**
	 * @return the valueKey
	 */
	public String getValueKey() {
		return valueKey;
	}
	/**
	 * @param valueKey the valueKey to set
	 */
	public void setValueKey(String valueKey) {
		this.valueKey = valueKey;
	}

}
