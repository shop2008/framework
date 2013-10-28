/**
 * 
 */
package com.wxxr.mobile.core.tools.generator;

/**
 * @author neillin
 *
 */
public class MenuItemModel {
	private String id,label,icon,description,enableWhen,visibleWhen;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the enableWhen
	 */
	public String getEnableWhen() {
		return enableWhen;
	}

	/**
	 * @return the visibleWhen
	 */
	public String getVisibleWhen() {
		return visibleWhen;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param enableWhen the enableWhen to set
	 */
	public void setEnableWhen(String enableWhen) {
		this.enableWhen = enableWhen;
	}

	/**
	 * @param visibleWhen the visibleWhen to set
	 */
	public void setVisibleWhen(String visibleWhen) {
		this.visibleWhen = visibleWhen;
	}

}
