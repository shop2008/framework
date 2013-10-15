/*
 * Copyright 2010-2013 WXXR Network Technology
 * Co. Ltd. All rights reserved. WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.mobile.core.tools.generator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.annotation.DBCommand;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.dao.JPACommand;

/**
 * @class desc DAOModel.
 * @author wangxuyang
 * @version $Revision$
 * @created time 2013-10-10 上午11:43:45
 */
public class DAOModel extends AbstractClassModel {
	private String daoIfPkg;
	private String daoIfName;
	private List<String> methodImpls = new ArrayList<String>();
	private Map<String, Method> methods = new HashMap<String, Method>();
	private Map<String, Class<? extends JPACommand>> commandClasses = new HashMap<String, Class<? extends JPACommand>>();
	public Map<String, Method> getMethods() {
		return methods;
	}
	public void setMethods(Map<String, Method> methods) {
		this.methods = methods;
	}
	public Map<String, Class<? extends JPACommand>> getCommandClasses() {
		return commandClasses;
	}
	public void setCommandClasses(Map<String, Class<? extends JPACommand>> commandClasses) {
		this.commandClasses = commandClasses;
	}
	public List<String> getMethodImpls() {
		return methodImpls;
	}
	public void setMethodImpls(List<String> methodImpls) {
		this.methodImpls = methodImpls;
	}
	public String getDaoIfPkg() {
		return daoIfPkg;
	}
	public void setDaoIfPkg(String daoIfPkg) {
		this.daoIfPkg = daoIfPkg;
	}
	public String getDaoIfName() {
		return daoIfName;
	}
	public void setDaoIfName(String daoIfName) {
		this.daoIfName = daoIfName;
	}	
	public void addMethod(Method method){
		methods.put(method.toString(), method);
		addImports(method);
		DBCommand ann = method.getAnnotation(DBCommand.class);
		if (ann!=null) {
			Class<?> commandClass = ann.clazz();
			if (commandClass!=null&&commandClass.isAssignableFrom(JPACommand.class)) {
				addImport(commandClass.getCanonicalName());
				String methodImpl = createMethodImpl(method, commandClass);
				if (StringUtils.isNotBlank(methodImpl)) {
					methodImpls.add(methodImpl);
				}
			}
		}else{
			System.out.println(method.getName());
		}
		
	}
	private void addImports(Method method) {
		Class<?> returnType = method.getReturnType();
		if (returnType!=null) {
			addImport(returnType.getCanonicalName());
		}
		Class<?>[] paramTypes = method.getParameterTypes();
		if (paramTypes!=null&&paramTypes.length>0) {
			for (Class<?> paramType : paramTypes) {
				addImport(paramType.getCanonicalName());
			}
		}
	}
	
	private String createMethodImpl(Method method,Class<?> commandClass){
		if (method==null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();	
		sb.append("public void ");
		sb.append(method.getName());
		sb.append("{}");
		return sb.toString();
	}

}
