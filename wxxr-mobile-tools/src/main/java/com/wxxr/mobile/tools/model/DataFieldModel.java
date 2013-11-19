/**
 * 
 */
package com.wxxr.mobile.tools.model;


/**
 * @author neillin
 *
 */
public class DataFieldModel extends AttributedFieldModel {
	private String valueKey;
	private FieldBindingModel binding;
	private ConvertorField convertor;
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

	/**
	 * @return the binding
	 */
	public FieldBindingModel getBinding() {
		return binding;
	}
	/**
	 * @param binding the binding to set
	 */
	public void setBinding(FieldBindingModel binding) {
		this.binding = binding;
	}
	/**
	 * @return the convertor
	 */
	public ConvertorField getConvertor() {
		return convertor;
	}
	/**
	 * @param convertor the convertor to set
	 */
	public void setConvertor(ConvertorField convertor) {
		this.convertor = convertor;
	}

}
