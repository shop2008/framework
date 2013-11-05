/**
 * 
 */
package com.wxxr.mobile.core.model;

/**
 * @author neillin
 *
 */
public class MenuItemModel implements JavaModel{
	private String name,label,icon,description,enableWhen,visibleWhen;
	private UICommandModel commandModel;

	/**
	 * @return the id
	 */
	public String getName() {
		return name;
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
	public void setName(String id) {
		this.name = id;
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

	/**
	 * @return the commandModel
	 */
	public UICommandModel getUICommandModel() {
		return commandModel;
	}

	/**
	 * @param commandModel the commandModel to set
	 */
	public void setUICommandModel(UICommandModel menu) {
		this.commandModel = menu;
		if(this.enableWhen != null){
			ViewModelUtils.createExpressionModel(((ViewModelClass)menu.getClassModel()), this, "enabled", this.enableWhen);
		}
		if(this.visibleWhen != null){
			ViewModelUtils.createExpressionModel(((ViewModelClass)menu.getClassModel()), this, "visible", this.visibleWhen);
		}
	}

}
