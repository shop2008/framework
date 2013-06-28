package com.wxxr.mobile.core.tools.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.wxxr.mobile.core.util.StringUtils;

public class AbstractClassModel {
	private String name;
	private String pkgName;
	private List<String> imports;
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
	/**
	 * @param imports the imports to set
	 */
	public void setImports(List<String> imports) {
		this.imports = imports;
	}
	
	public void addImport(String stmt){
		if(StringUtils.isBlank(stmt)){
			return;
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
		}
	}
	
	
}
