/**
 * 
 */
package com.wxxr.mobile.tools.model;

import java.util.LinkedList;

import com.wxxr.mobile.core.ui.api.INavigationDescriptor;

/**
 * @author neillin
 *
 */
public class WorkbenchModel extends AbstractClassModel {
	private String workbenchClass;
	private LinkedList<NavigationModel> navigations;
	private String title, description;


	
	public void addNavigation(NavigationModel item){
		if(this.navigations == null){
			this.navigations = new LinkedList<NavigationModel>();
		}
		if(!this.navigations.contains(item)){
			this.navigations.add(item);
		}
	}

	public NavigationModel[] getNavigations() {
		return this.navigations != null && this.navigations.isEmpty() == false ? 
				this.navigations.toArray(new NavigationModel[this.navigations.size()]) :
				null;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the workbenchClass
	 */
	public String getWorkbenchClass() {
		return workbenchClass;
	}

	/**
	 * @param workbenchClass the workbenchClass to set
	 */
	public void setWorkbenchClass(String workbenchClass) {
		this.workbenchClass = addImport(workbenchClass);;
	}
	
}
