/**
 * 
 */
package com.wxxr.mobile.tools.model;

import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;

/**
 * @author neillin
 *
 */
public class TypeModel {

	private String type;
	private TypeModel[] parameterTypes;
	
	
	public TypeModel(String type, TypeModel[] parameterTypes) {
		super();
		this.type = type;
		this.parameterTypes = parameterTypes;
	}

	public TypeModel(String typeString){
		int idx = typeString.indexOf('<');
		if(idx > 0){
			this.type = typeString.substring(0,idx);
			String[] pTypes = parseParamterTypes(typeString.substring(idx));
			if((pTypes != null)&&(pTypes.length > 0)){
				parameterTypes = new TypeModel[pTypes.length];
				for (int i=0 ; i < pTypes.length ; i++) {
					parameterTypes[i] = new TypeModel(pTypes[i]);
				}
			}
		}else{
			this.type = typeString;
		}
	}
	
	String[] parseParamterTypes(String s){
		s = StringUtils.trimToNull(s);
		s = s.substring(1,s.length()-1);	// remove < and >
		LinkedList<String> result = null;
		int len = s.length();
		int pos = 0;
		int depth = 0;
		for(int i=0 ; i < len ; i++){
			char ch = s.charAt(i);
			switch(ch){
			case '<':
				depth++;
				break;
			case '>':
				depth--;
				break;
			case ',':
				if(depth == 0){
					String val = StringUtils.trimToNull(s.substring(pos,i));
					if(val != null){
						if(result == null){
							result = new LinkedList<String>();
						}
						result.add(val);
					}
					pos = i+1;
				}
				break;
			}
		}
		String val = StringUtils.trimToNull(s.substring(pos,s.length()));
		if(val != null){
			if(result == null){
				result = new LinkedList<String>();
			}
			result.add(val);
		}
		return result != null ? result.toArray(new String[0]) : null;		
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @return the parameterTypes
	 */
	public TypeModel[] getParameterTypes() {
		return parameterTypes;
	}
	
	public String getSimpleTypeName(AbstractClassModel model) {
		return model.addImport(getType());
	}
	
	public String toSimpleName(AbstractClassModel model){
		StringBuffer buf = new StringBuffer();
		buf.append(model.addImport(getType()));
		if((this.parameterTypes != null)&&(this.parameterTypes.length > 0)){
			buf.append('<');
			int cnt = 0 ;
			for (int i = 0; i < this.parameterTypes.length; i++) {
				TypeModel p = this.parameterTypes[i];
				if(cnt > 0){
					buf.append(',');
				}
				buf.append(p.toSimpleName(model));
				cnt++;
			}
			buf.append('>');
		}
		return buf.toString();
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
