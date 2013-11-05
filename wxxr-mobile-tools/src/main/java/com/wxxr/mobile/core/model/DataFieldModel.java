/**
 * 
 */
package com.wxxr.mobile.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

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
		this.enableWhenExpress = StringUtils.trimToNull(enableWhenExpress);
		if(getClassModel() != null){
			ViewModelUtils.createExpressionModel((ViewModelClass)getClassModel(), this, "enabled", this.enableWhenExpress);
		}
	}
	/**
	 * @param visibleWhenExpress the visibleWhenExpress to set
	 */
	public void setVisibleWhenExpress(String visibleWhenExpress) {
		this.visibleWhenExpress = StringUtils.trimToNull(visibleWhenExpress);
		if(getClassModel() != null){
			ViewModelUtils.createExpressionModel((ViewModelClass)getClassModel(), this, "visible", this.visibleWhenExpress);
		}
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
		Parameter p = new Parameter(key, StringUtils.trimToNull(value));
		this.attributes.put(p.getName(), p);
		if(getClassModel() != null){
			ViewModelUtils.createExpressionModel((ViewModelClass)getClassModel(), this, key, p.getValue());
		}
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
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.model.FieldModel#setClassModel(com.wxxr.mobile.core.model.AbstractClassModel)
	 */
	@Override
	public void setClassModel(AbstractClassModel classModel) {
		super.setClassModel(classModel);
		ViewModelClass model = (ViewModelClass)classModel;
		if(this.enableWhenExpress != null){
			ViewModelUtils.createExpressionModel(model, this, "enabled", this.enableWhenExpress);
		}
		if(this.visibleWhenExpress != null){
			ViewModelUtils.createExpressionModel(model, this, "visible", this.visibleWhenExpress);
		}
		if((this.attributes != null)&&(this.attributes.size() > 0)){
			for (Parameter p : this.attributes.values()) {
				ViewModelUtils.createExpressionModel(model, this, p.getName(), p.getValue());
			}
		}
	}

}
