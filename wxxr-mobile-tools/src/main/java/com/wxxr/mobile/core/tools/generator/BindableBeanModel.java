/**
 * 
 */
package com.wxxr.mobile.core.tools.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public class BindableBeanModel extends AbstractClassModel {
	private Map<String,FieldModel> fields;

	/**
	 * @return the fields
	 */
	public List<FieldModel> getFields() {
		return fields == null ? Collections.EMPTY_LIST : new ArrayList<FieldModel>(fields.values());
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(Map<String,FieldModel> fields) {
		this.fields = fields;
	}
	
	public void addField(String name, Class<?> type){
		name = StringUtils.trimToNull(name);
		if((name == null)||(type == null)){
			throw new IllegalArgumentException("name and type cannot be NULL !");
		}
		addField(name, type.getCanonicalName());
	}
	
	
	public void addField(String name, String className){
		name = StringUtils.trimToNull(name);
		className = StringUtils.trimToNull(className);
		if((name == null)||(className == null)){
			throw new IllegalArgumentException("name and className cannot be NULL !");
		}
		if(this.fields == null){
			this.fields = new HashMap<String, FieldModel>();
		}
		FieldModel fld = this.fields.get(name);
		if(fld == null){
			fld = new FieldModel();
			fld.setName(name);
			this.fields.put(name, fld);
		}
		int idx = className.lastIndexOf('.');
		String simpleName = className;
		if(idx >0){
			simpleName = className.substring(idx+1);
		}
		fld.setType(simpleName);
		addImport(className);
	}
	
}
