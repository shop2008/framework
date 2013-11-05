/**
 * 
 */
package com.wxxr.mobile.core.model;


/**
 * @author neillin
 *
 */
public class FieldModel implements MemberModel{
	private AbstractClassModel classModel;
	private String name;
	private String type;
	private String initializer;
	private String modifiers;
	private String javaStatement;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	public String getSimpleType() {
		return new TypeModel(getType()).getSimpleTypeName(classModel);
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the classModel
	 */
	public AbstractClassModel getClassModel() {
		return classModel;
	}
	/**
	 * @param classModel the classModel to set
	 */
	public void setClassModel(AbstractClassModel classModel) {
		this.classModel = classModel;
		this.classModel.addImport(type);
	}
	/**
	 * @return the initializer
	 */
	public String getInitializer() {
		return initializer;
	}
	/**
	 * @param initializer the initializer to set
	 */
	public void setInitializer(String initializer) {
		this.initializer = initializer;
	}
	/**
	 * @return the modifiers
	 */
	public String getModifiers() {
		return modifiers;
	}
	/**
	 * @param modifiers the modifiers to set
	 */
	public void setModifiers(String modifiers) {
		this.modifiers = modifiers;
	}
	
	public String toString() {
		return getJavaStatement();
	}
	
	@Override
	public String getJavaStatement() {
		if(this.javaStatement == null){
			StringBuffer buf = new StringBuffer();
			buf.append(this.modifiers != null ? this.modifiers : "private").append(' ');
			buf.append(this.classModel.addImport(this.type)).append(' ').append(this.name);
			if(this.initializer != null){
				buf.append(" = ").append(this.initializer);
			}
			this.javaStatement = buf.append(';').toString();
		}
		return this.javaStatement;
	}

	/**
	 * @param javaStatement the javaStatement to set
	 */
	public void setJavaStatement(String javaStatement) {
		this.javaStatement = javaStatement;
	}
	
	
}
