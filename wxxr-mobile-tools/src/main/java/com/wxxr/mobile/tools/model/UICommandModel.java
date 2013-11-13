/**
 * 
 */
package com.wxxr.mobile.tools.model;

import java.util.LinkedList;

import com.wxxr.mobile.core.ui.api.IProgressGuard;


/**
 * @author neillin
 *
 */
public class UICommandModel implements JavaModel{
	private ViewModelClass classModel;
	
	private String name;
	private String methodName;
	private String className;
	private String description;
	private String enabledWhenExpress,visibleWhenExpress;
	private LinkedList<MenuItemModel> menuItems;
	private LinkedList<NavigationModel> navigations;
	private IProgressGuard progressGuard;
	/**
	 * @return the commandName
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return the enabledWhenExpress
	 */
	public String getEnabledWhenExpress() {
		return enabledWhenExpress;
	}
	/**
	 * @return the visibleWhenExpress
	 */
	public String getVisibleWhenExpress() {
		return visibleWhenExpress;
	}
	/**
	 * @return the menuItems
	 */
	public MenuItemModel[] getMenuItems() {
		return menuItems != null ?  menuItems.toArray(new MenuItemModel[0]) : new MenuItemModel[0];
	}
	/**
	 * @return the navigations
	 */
	public NavigationModel[] getNavigations() {
		return navigations != null ? navigations.toArray(new NavigationModel[0]) : new NavigationModel[0];
	}
	/**
	 * @param commandName the commandName to set
	 */
	public void setName(String commandName) {
		this.name = commandName;
	}
	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @param enabledWhenExpress the enabledWhenExpress to set
	 */
	public void setEnabledWhenExpress(String enabledWhenExpress) {
		this.enabledWhenExpress = enabledWhenExpress;
	}
	/**
	 * @param visibleWhenExpress the visibleWhenExpress to set
	 */
	public void setVisibleWhenExpress(String visibleWhenExpress) {
		this.visibleWhenExpress = visibleWhenExpress;
	}
	
	public void addMenuItem(MenuItemModel item){
		if(this.menuItems == null){
			this.menuItems = new LinkedList<MenuItemModel>();
		}
		if(!this.menuItems.contains(item)){
			this.menuItems.add(item);
			item.setUICommandModel(this);
		}
	}
	
	public void addNavigation(NavigationModel item){
		if(this.navigations == null){
			this.navigations = new LinkedList<NavigationModel>();
		}
		if(!this.navigations.contains(item)){
			this.navigations.add(item);
		}
	}
	
	/**
	 * @return the classModel
	 */
	public AbstractClassModel getClassModel() {
		return classModel;
	}
	/**
	 * @param classModel the classModel to set
	 */
	public void setClassModel(AbstractClassModel classModel) {
		this.classModel = (ViewModelClass)classModel;
		if(this.enabledWhenExpress != null){
			ViewModelUtils.createExpressionModel(this.classModel, this, "enabled", this.enabledWhenExpress);
		}
		if(this.visibleWhenExpress != null){
			ViewModelUtils.createExpressionModel(this.classModel, this, "visible", this.visibleWhenExpress);
		}
	}
	/**
	 * @return the progressGuard
	 */
	public IProgressGuard getProgressGuard() {
		return progressGuard;
	}
	/**
	 * @param progressGuard the progressGuard to set
	 */
	public void setProgressGuard(IProgressGuard progressGuard) {
		this.progressGuard = progressGuard;
	}

}
