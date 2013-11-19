/**
 * 
 */
package com.wxxr.mobile.tools.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.ui.annotation.ValueType;

/**
 * @author neillin
 *
 */
public class ConvertorField extends FieldModel {
	private String className;
	private Map<String, Parameter> params;
	private String sourceValueType, targetValueType;

	
	public void addParameter(String name, String val,ValueType type){
		if(this.params == null){
			this.params = new HashMap<String, Parameter>();
		}
		Parameter p = new Parameter(name,val);
		p.setType(type);
		this.params.put(p.getName(), p);
	}

	/**
	 * @return the params
	 */
	public List<Parameter> getParams() {
		return params != null ? new ArrayList<Parameter>(params.values()) : null;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the sourceValueType
	 */
	public String getSourceValueType() {
		return sourceValueType;
	}

	public String getSimpleSourceType() {
		return getSimpleBoxedType(getSourceValueType());
	}
	/**
	 * @return the targetValueType
	 */
	public String getTargetValueType() {
		return targetValueType;
	}

	public String getSimpleTargetType() {
		return getSimpleBoxedType(getTargetValueType());
	}
	
	/**
	 * @param sourceValueType the sourceValueType to set
	 */
	public void setSourceValueType(String sourceValueType) {
		this.sourceValueType = sourceValueType;
	}

	/**
	 * @param targetValueType the targetValueType to set
	 */
	public void setTargetValueType(String targetValueType) {
		this.targetValueType = targetValueType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConvertorField [className=" + className + ", params=" + params
				+ ", sourceValueType=" + sourceValueType + ", targetValueType="
				+ targetValueType + "fieldname="+getName()+"fieldType="+getType()+"]";
	}
	
	

}
