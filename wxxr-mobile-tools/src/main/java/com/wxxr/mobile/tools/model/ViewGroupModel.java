/**
 * 
 */
package com.wxxr.mobile.tools.model;


/**
 * @author neillin
 *
 */
public class ViewGroupModel extends AttributedFieldModel{
	private String[] viewIds;
	private boolean dynamic;
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
