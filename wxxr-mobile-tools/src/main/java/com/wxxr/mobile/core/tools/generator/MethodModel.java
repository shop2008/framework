/**
 * 
 */
package com.wxxr.mobile.core.tools.generator;

/**
 * @author neillin
 *
 */
public class MethodModel implements JavaModel{
	private AbstractClassModel classModel;
	private String methodName;
	private String returnType;
	private String[] parameterTypes;
	private String[] parameterNames;
	private String[] thrownTypes;
	private String methodBody;
	private String modifiers;
	private boolean varArgs;
	
	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}
	/**
	 * @return the returnType
	 */
	public String getReturnType() {
		return returnType;
	}
	/**
	 * @return the parameterTypes
	 */
	public String[] getParameterTypes() {
		return parameterTypes;
	}
	/**
	 * @return the parameterNames
	 */
	public String[] getParameterNames() {
		return parameterNames;
	}
	/**
	 * @return the thrownTypes
	 */
	public String[] getThrownTypes() {
		return thrownTypes;
	}
	/**
	 * @return the methodBody
	 */
	public String getMethodBody() {
		return methodBody;
	}
	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	/**
	 * @param returnType the returnType to set
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	/**
	 * @param parameterTypes the parameterTypes to set
	 */
	public void setParameterTypes(String[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	/**
	 * @param parameterNames the parameterNames to set
	 */
	public void setParameterNames(String[] parameterNames) {
		this.parameterNames = parameterNames;
	}
	/**
	 * @param thrownTypes the thrownTypes to set
	 */
	public void setThrownTypes(String[] thrownTypes) {
		this.thrownTypes = thrownTypes;
	}
	/**
	 * @param methodBody the methodBody to set
	 */
	public void setMethodBody(String methodBody) {
		this.methodBody = methodBody;
	}
	/**
	 * @return the varArgs
	 */
	public boolean isVarArgs() {
		return varArgs;
	}
	/**
	 * @param varArgs the varArgs to set
	 */
	public void setVarArgs(boolean varArgs) {
		this.varArgs = varArgs;
	}
	
	public String toString() {
		return toJavaStatement();
	}
	/**
	 * @param buf
	 */
	protected void generateMethodKey(StringBuffer buf) {
		buf.append(getMethodName()).append('(');
		int size = this.parameterTypes != null ? this.parameterTypes.length : 0;
		if(size > 0){
			for(int i=0 ; i < size ; i++){
				if(i > 0){
					buf.append(',');
				}
				buf.append(this.classModel.addImport(this.parameterTypes[i]));
				if(i == (size -1)){
					if(this.varArgs){
						buf.append("...");
					}
				}
			}
		}
		buf.append(')');
	}
	
	public String getMethodKey() {
		StringBuffer buf = new StringBuffer();
		generateMethodKey(buf);
		return buf.toString();
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
	}
	@Override
	public String toJavaStatement() {
		StringBuffer buf = new StringBuffer();
		if(getModifiers() != null){
			buf.append(getModifiers()).append(' ');
		}
		buf.append(this.classModel.addImport(this.returnType != null ? this.returnType : "void")).append(' ');
		buf.append(getMethodName()).append('(');
		int size = this.parameterTypes != null ? this.parameterTypes.length : 0;
		if(size > 0){
			for(int i=0 ; i < size ; i++){
				if(i > 0){
					buf.append(',');
				}
				buf.append(this.classModel.addImport(this.parameterTypes[i]));
				if(i == (size -1)){
					if(this.varArgs){
						buf.append("...");
					}
				}
				buf.append(' ').append(this.parameterNames[i]);
			}
		}
		buf.append(')');
		size = this.thrownTypes != null ? this.thrownTypes.length : 0;
		if(size > 0){
			buf.append(" throws ");
			for (int i = 0; i < size; i++) {
				if(i > 0){
					buf.append(',');
				}
				buf.append(this.classModel.addImport(this.thrownTypes[i]));
			}
		}
		return buf.toString();
	}
}
