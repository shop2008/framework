/**
 * 
 */
package com.wxxr.mobile.tools.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.ui.common.ListDecorator;
import com.wxxr.mobile.core.ui.common.MapDecorator;
import com.wxxr.mobile.core.ui.common.SetDecorator;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public class BindableBeanModel extends AbstractClassModel {
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
		BindableField fld = new BindableField();
		fld.setName(name);
		addField(fld);
		TypeModel type = new TypeModel(className); //.toSimpleName(this)
		fld.setType(type.toSimpleName(this));
		if("java.util.List".equals(type.getType())){
			type.setType(ListDecorator.class.getCanonicalName());
			fld.setDecoratedType(type.toSimpleName(this));
		}else if("java.util.Set".equals(type.getType())){
			type.setType(SetDecorator.class.getCanonicalName());
			fld.setDecoratedType(type.toSimpleName(this));
		}else if("java.util.Map".equals(type.getType())){
			type.setType(MapDecorator.class.getCanonicalName());
		}
	}
	
}
