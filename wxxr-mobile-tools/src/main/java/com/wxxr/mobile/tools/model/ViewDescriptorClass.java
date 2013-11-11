/**
 * 
 */
package com.wxxr.mobile.tools.model;

/**
 * @author neillin
 *
 */
public class ViewDescriptorClass extends AbstractClassModel {
	private ViewModelClass viewModel;
	private TargetUIClass targetUI;
	private String layoutId;
	private String bindingType;

	/**
	 * @return the viewModel
	 */
	public ViewModelClass getViewModel() {
		return viewModel;
	}

	/**
	 * @param viewModel the viewModel to set
	 */
	public void setViewModel(ViewModelClass viewModel) {
		this.viewModel = viewModel;
	}

	/**
	 * @return the targetUI
	 */
	public TargetUIClass getTargetUI() {
		return targetUI;
	}

	/**
	 * @param targetUI the targetUI to set
	 */
	public void setTargetUI(TargetUIClass targetUI) {
		this.targetUI = targetUI;
	}

	/**
	 * @return the layoutId
	 */
	public String getLayoutId() {
		return layoutId;
	}

	/**
	 * @param layoutId the layoutId to set
	 */
	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}

	/**
	 * @return the bindingType
	 */
	public String getBindingType() {
		return bindingType;
	}

	/**
	 * @param bindingType the bindingType to set
	 */
	public void setBindingType(String bindingType) {
		this.bindingType = bindingType;
	}
}
