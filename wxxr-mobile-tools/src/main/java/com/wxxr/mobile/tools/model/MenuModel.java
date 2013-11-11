/**
 * 
 */
package com.wxxr.mobile.tools.model;


/**
 * @author neillin
 *
 */
public class MenuModel extends AttributedFieldModel{
	private String description;
	private String[] items;
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
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
	 * @param items the items to set
	 */
	public void setItems(String[] items) {
		this.items = items;
	}	
}
