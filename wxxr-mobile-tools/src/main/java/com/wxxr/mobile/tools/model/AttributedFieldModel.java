/**
 * 
 */
package com.wxxr.mobile.tools.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxxr.mobile.core.tools.generator.UIViewModelGenerator;

/**
 * @author neillin
 *
 */
public abstract class AttributedFieldModel extends FieldModel {
	private static final Logger log = LoggerFactory.getLogger(AttributedFieldModel.class);

	private String enableWhenExpress;
	private String visibleWhenExpress;
	private Map<String, Parameter> attributes;
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
		if(this.enableWhenExpress != null){
			addAttribute("enabled", this.enableWhenExpress);
		}
	}
	/**
	 * @param visibleWhenExpress the visibleWhenExpress to set
	 */
	public void setVisibleWhenExpress(String visibleWhenExpress) {
		this.visibleWhenExpress = StringUtils.trimToNull(visibleWhenExpress);
		if(this.visibleWhenExpress != null){
			addAttribute("visible", this.visibleWhenExpress);
		}
	}
	
	/**
	 * @return the params
	 */
	public List<Parameter> getAttributes() {
		return this.attributes != null ? new ArrayList<Parameter>(attributes.values()) : null;
	}

	public void addAttribute(String key,String value){
//		log.info("add attribute, key :"+key+", value :"+value);
		if(this.attributes == null){
			this.attributes = new HashMap<String, Parameter>();
		}
		Parameter p = new Parameter(key, StringUtils.trimToNull(value));
		this.attributes.put(p.getName(), p);
		if(getClassModel() != null){
			ViewModelUtils.createExpressionModel((ViewModelClass)getClassModel(), this, key, p.getValue());
		}
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.tools.model.FieldModel#setClassModel(com.wxxr.mobile.tools.model.AbstractClassModel)
	 */
	@Override
	public void setClassModel(AbstractClassModel classModel) {
		super.setClassModel(classModel);
		ViewModelClass model = (ViewModelClass)classModel;
		if((this.attributes != null)&&(this.attributes.size() > 0)){
			for (Parameter p : this.attributes.values()) {
				ViewModelUtils.createExpressionModel(model, this, p.getName(), p.getValue());
			}
		}
	}
}
