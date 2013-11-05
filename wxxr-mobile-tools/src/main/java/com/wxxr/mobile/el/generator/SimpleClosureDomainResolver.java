package com.wxxr.mobile.el.generator;


import javax.lang.model.type.*;

import com.wxxr.mobile.core.tools.ICodeGenerationContext;

@SuppressWarnings("restriction")
public class SimpleClosureDomainResolver implements JavaDomainResolver {

	final private JavaDomainResolver resolver;
	final private TypeMirror identifierType;
	private String identifier;


	public SimpleClosureDomainResolver(JavaDomainResolver resolv,TypeMirror inType){
		this.resolver = resolv;
		this.identifierType = inType;
	}

	/**
	 * @param ident
	 * @return
	 * @see com.wxx.el.java.JavaDomainResolver#resoveIdentifierType(java.lang.String)
	 */
	public TypeMirror resoveIdentifierType(String ident) {
		return this.identifierType;
	}

	/**
	 * @param ident
	 * @return
	 * @see com.wxx.el.java.JavaDomainResolver#generateGetIdentifierStatement(java.lang.String)
	 */
	public String generateGetIdentifierStatement(String ident) {
		this.identifier = ident;
		return this.identifier;
	}

	/**
	 * @param source
	 * @param propertyName
	 * @return
	 * @see com.wxx.el.java.JavaDomainResolver#resolvePropertyType(java.lang.Class, java.lang.String)
	 */
	public TypeMirror resolvePropertyType(String className , String propertyName) {
		return resolver.resolvePropertyType(className, propertyName);
	}

	/**
	 * @param source
	 * @param propertyName
	 * @return
	 * @see com.wxx.el.java.JavaDomainResolver#generateGetPropertyStatement(java.lang.Class, java.lang.String)
	 */
	public String generateGetPropertyStatement(String className,
			String propertyName) {
		return resolver.generateGetPropertyStatement(className, propertyName);
	}

	/**
	 * @param source
	 * @param methodName
	 * @param paramTypes
	 * @return
	 * @see com.wxx.el.java.JavaDomainResolver#resolveMethodReturnType(java.lang.Class, java.lang.String, java.lang.Class<?>[])
	 */
	public TypeMirror resolveMethodReturnType(String className, String methodName,
			TypeMirror[] paramTypes) {
		return resolver.resolveMethodReturnType(className, methodName, paramTypes);
	}

	/**
	 * @param op1
	 * @param op2
	 * @return
	 * @see com.wxx.el.java.JavaDomainResolver#resolveMultiplyResultType(java.lang.Class, java.lang.Class)
	 */
	public TypeMirror resolveMultiplyResultType(TypeMirror op1, TypeMirror op2) {
		return resolver.resolveMultiplyResultType(op1, op2);
	}

	/**
	 * @param source
	 * @param methodName
	 * @param paramNames
	 * @return
	 * @see com.wxx.el.java.JavaDomainResolver#generateMethodCallStatement(java.lang.Class, java.lang.String, java.lang.String[])
	 */
	public String generateMethodCallStatement(String className,
			String methodName, String[] paramNames) {
		return resolver.generateMethodCallStatement(className, methodName,
				paramNames);
	}

	/**
	 * @param type
	 * @return
	 * @see com.wxx.el.java.JavaDomainResolver#getDefaultValueOfType(java.lang.Class)
	 */
	public String getDefaultValueOfType(TypeMirror type) {
		return resolver.getDefaultValueOfType(type);
	}

	/**
	 * @param type
	 * @return
	 * @see com.wxx.el.java.JavaDomainResolver#resolveCollectionType(java.lang.Class)
	 */
	public TypeMirror resolveCollectionType(TypeMirror type) {
		return resolver.resolveCollectionType(type);
	}

	/**
	 * Get the identifier.
	 * 
	 * @return the identifier.
	 */
	public String getIdentifier() {
		return identifier;
	}

	public String getNextVarName() {
		return resolver.getNextVarName();
	}

	/**
	 * @param type
	 * @return
	 * @see com.wxxr.mobile.el.generator.JavaDomainResolver#isTypeOfComparable(javax.lang.model.type.TypeMirror)
	 */
	public boolean isTypeOfComparable(TypeMirror type) {
		return resolver.isTypeOfComparable(type);
	}

	/**
	 * @param type
	 * @return
	 * @see com.wxxr.mobile.el.generator.JavaDomainResolver#isBooleanType(javax.lang.model.type.TypeMirror)
	 */
	public boolean isBooleanType(TypeMirror type) {
		return resolver.isBooleanType(type);
	}

	/**
	 * @param type
	 * @return
	 * @see com.wxxr.mobile.el.generator.JavaDomainResolver#isNumberType(javax.lang.model.type.TypeMirror)
	 */
	public boolean isNumberType(TypeMirror type) {
		return resolver.isNumberType(type);
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.el.generator.JavaDomainResolver#getContext()
	 */
	public ICodeGenerationContext getContext() {
		return resolver.getContext();
	}
}
