/**
 * 
 */
package com.wxxr.mobile.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author neillin
 *
 */
public class ViewGroupModel extends FieldModel{
	private String[] viewIds;
	private boolean dynamic;
	private String enableWhenExpress;
	private String visibleWhenExpress;
	private Map<String,Parameter> attributes;
	private String id;
	private String defaultViewId;
	
	/**
	 * @return the viewIds
	 */
	public String[] getViewIds() {
		return viewIds;
	}
	/**
	 * @return the dynamic
	 */
	public boolean isDynamic() {
		return dynamic;
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
	 * @param viewIds the viewIds to set
	 */
	public void setViewIds(String[] viewIds) {
		this.viewIds = viewIds;
	}
	/**
	 * @param dynamic the dynamic to set
	 */
	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the defaultViewId
	 */
	public String getDefaultViewId() {
		return defaultViewId;
	}
	/**
	 * @param defaultViewId the defaultViewId to set
	 */
	public void setDefaultViewId(String defaultViewId) {
		this.defaultViewId = defaultViewId;
	}
	
}
