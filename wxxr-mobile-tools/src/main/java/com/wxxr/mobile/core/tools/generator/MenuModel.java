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
public class MenuModel extends FieldModel{
	private String description;
	private String enableWhenExpress;
	private String visibleWhenExpress;
	private String[] items;
	private Map<String,Parameter> attributes;
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
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
	 * @return the items
	 */
	public String[] getItems() {
		return items;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @param items the items to set
	 */
	public void setItems(String[] items) {
		this.items = items;
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
}
