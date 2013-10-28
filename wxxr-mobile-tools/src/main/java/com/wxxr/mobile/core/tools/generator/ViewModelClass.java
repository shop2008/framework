/**
 * 
 */
package com.wxxr.mobile.core.tools.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public class ViewModelClass extends AbstractClassModel {

	private Map<String,FieldModel> fields;
	private Map<String, MethodModel> methods;
	private Map<String,UICommandModel> commandModels;
	private boolean traceRequired;
	private String applicationId;
	private String id;
	private boolean isPage;
	/**
	 * @return the fields
	 */
	public List<FieldModel> getFields() {
		return fields == null ? null : new ArrayList<FieldModel>(fields.values());
	}

	public List<MethodModel> getMethods() {
		return methods == null ? null : new ArrayList<MethodModel>(methods.values());
	}
	
	public List<UICommandModel> getCommands() {
		return commandModels == null ? null : new ArrayList<UICommandModel>(commandModels.values());
	}
	
	public List<MenuModel> getMenus() {
		if((this.fields == null)||(this.fields.size() == 0)){
			return null;
		}
		ArrayList<MenuModel> menus = new ArrayList<MenuModel>();
		for (FieldModel field : this.fields.values()) {
			if(field instanceof MenuModel){
				menus.add((MenuModel)field);
			}
		}
		return menus.isEmpty() ? null : menus;
	}

	
	public List<DataFieldModel> getDataFields() {
		if((this.fields == null)||(this.fields.size() == 0)){
			return null;
		}
		ArrayList<DataFieldModel> result = new ArrayList<DataFieldModel>();
		for (FieldModel field : this.fields.values()) {
			if(field instanceof DataFieldModel){
				result.add((DataFieldModel)field);
			}
		}
		return result.isEmpty() ? null : result;
	}

	
	public List<ViewGroupModel> getViewGroups() {
		if((this.fields == null)||(this.fields.size() == 0)){
			return null;
		}
		ArrayList<ViewGroupModel> result = new ArrayList<ViewGroupModel>();
		for (FieldModel field : this.fields.values()) {
			if(field instanceof ViewGroupModel){
				result.add((ViewGroupModel)field);
			}
		}
		return result.isEmpty() ? null : result;
	}

	public void addField(String name, Class<?> type){
		name = StringUtils.trimToNull(name);
		if((name == null)||(type == null)){
			throw new IllegalArgumentException("name and type cannot be NULL !");
		}
		addField(name, type.getCanonicalName());
	}
	
	public FieldModel getField(String name){
		return this.fields != null ? this.fields.get(name) : null;
	}
	
	public boolean isTraceRequired() {
		return traceRequired;
	}
	
	public void addField(String name, String className){
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
		if(field.getName().equals("log")){
			this.traceRequired = true;
			return;
		}
		if(this.fields == null){
			this.fields = new HashMap<String, FieldModel>();
		}
		this.fields.put(field.getName(), field);
		field.setClassModel(this);
	}
	
	public void addMethod(MethodModel method){
		if(this.methods == null){
			this.methods = new HashMap<String, MethodModel>();
		}
		method.setClassModel(this);
		this.methods.put(method.getMethodKey(), method);
	}
	
	
	public void addCommandModel(UICommandModel cmd){
		if(this.commandModels == null){
			this.commandModels = new HashMap<String, UICommandModel>();
		}
		cmd.setClassModel(this);
		this.commandModels.put(cmd.getCommandName(), cmd);
	}

	/**
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public void prepare() {
		List<DataFieldModel> dFields = getDataFields();
		if(dFields != null){
			for (DataFieldModel f : dFields) {
				this.fields.remove(f.getName()+"Field");
			}
		}
	}

	/**
	 * @return the isPage
	 */
	public boolean isPage() {
		return isPage;
	}

	/**
	 * @param isPage the isPage to set
	 */
	public void setPage(boolean isPage) {
		this.isPage = isPage;
	}

}
