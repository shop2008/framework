package com.wxxr.mobile.tools.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.util.StringUtils;

public class AbstractClassModel {
	private String name;
	private String pkgName;
	private List<String> imports;
	private String superClass;
	private List<String> interfaces;
	protected Map<String,FieldModel> fields;
	private Map<String, MethodModel> methods;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the pkgName
	 */
	public String getPkgName() {
		return pkgName;
	}
	/**
	 * @return the imports
	 */
	public List<String> getImports() {
		return imports == null ? Collections.EMPTY_LIST : imports;
	}
	
	public String getClassName() {
		return this.pkgName+"."+name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param pkgName the pkgName to set
	 */
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	
	public String addImport(String stmt){
		if(StringUtils.isBlank(stmt)){
			return null;
		}
		String generic = null;
		int sIdx = stmt.indexOf('<');
		if(sIdx > 0){
			generic = StringUtils.trimToNull(stmt.substring(sIdx+1));
			if(generic.endsWith(">")){
				generic = generic.substring(0,generic.length()-1);
			}
			stmt = stmt.substring(0,sIdx);
		}
		int idx = stmt.lastIndexOf('.');
		if(idx > 0){
			String pkg = stmt.substring(0,idx);
			if((!"java.lang".equals(pkg))&&(!pkg.equals(this.pkgName))){
				if(imports == null){
					imports = new ArrayList<String>();
				}
				if(!imports.contains(stmt)){
					imports.add(stmt);
				}
			}
			stmt = stmt.substring(idx+1);
		}
		if(generic != null){
			String[] tokens = StringUtils.split(generic, ',');
			StringBuffer buf = new StringBuffer(stmt).append('<');
			int cnt = 0;
			for (String token : tokens) {
				token = addImport(token);
				if(cnt > 0){
					buf.append(',');
				}
				buf.append(token);
				cnt++;
			}
			stmt = buf.append('>').toString();
		}
		return stmt;
	}
	/**
	 * @return the superClass
	 */
	public String getSuperClass() {
		return superClass;
	}
	/**
	 * @return the interfaces
	 */
	public List<String> getInterfaces() {
		return interfaces;
	}
	
	public String getJoinInterfaces() {
		return this.interfaces != null && this.interfaces.size() > 0 ? StringUtils.join(this.interfaces.iterator(), ',') : null;
	}
	/**
	 * @param superClass the superClass to set
	 */
	public void setSuperClass(String superClass) {
		this.superClass = addImport(superClass);
	}
	/**
	 * @param interfaces the interfaces to set
	 */
	public void addInterface(String clazz) {
		clazz = addImport(clazz);
		if(this.interfaces == null){
			this.interfaces = new ArrayList<String>();
		}
		if(!this.interfaces.contains(clazz)){
			this.interfaces.add(clazz);
		}
	}
	/**
	 * @return the fields
	 */
	public List<FieldModel> getFields() {
		return fields == null ? null : new ArrayList<FieldModel>(fields.values());
	}
	public List<MethodModel> getMethods() {
		return methods == null ? null : new ArrayList<MethodModel>(methods.values());
	}
	public void addField(String name, String className) {
		name = StringUtils.trimToNull(name);
		className = StringUtils.trimToNull(className);
		if((name == null)||(className == null)){
			throw new IllegalArgumentException("name and className cannot be NULL !");
		}
		DataFieldModel fld = new DataFieldModel();
		fld.setName(name);
		fld.setType(className);
		addField(fld);
	}
	/**
	 * 
	 */
	public void addField(FieldModel field) {
		if(this.fields == null){
			this.fields = new HashMap<String, FieldModel>();
		}
		this.fields.put(field.getName(), field);
		field.setClassModel(this);
	}
	public void addMethod(MethodModel method) {
		if(this.methods == null){
			this.methods = new HashMap<String, MethodModel>();
		}
		method.setClassModel(this);
		this.methods.put(method.getMethodKey(), method);
	}
	
	
}
