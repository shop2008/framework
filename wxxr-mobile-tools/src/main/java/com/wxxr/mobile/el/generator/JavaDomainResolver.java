/*
 * @(#)JavaDomainResolver.java May 11, 2011 Copyright 2004-2011 WXXR Network
 * Technology Co. Ltd. All rights reserved. WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.el.generator;

import javax.lang.model.type.*;

import com.wxxr.mobile.core.tools.ICodeGenerationContext;


@SuppressWarnings("restriction")
public interface JavaDomainResolver {

public TypeMirror resoveIdentifierType(String ident);

   public String generateGetIdentifierStatement(String ident);

   public TypeMirror resolvePropertyType(String className, String propertyName);

   public String generateGetPropertyStatement(String className, String propertyName);

   public TypeMirror resolveMethodReturnType(String className, String methodName, TypeMirror[] paramTypes);

   public TypeMirror resolveMultiplyResultType(TypeMirror op1, TypeMirror op2);

   public String generateMethodCallStatement(String className, String methodName, String[] paramNames);

   public String getDefaultValueOfType(TypeMirror type);
   
   public TypeMirror resolveCollectionType(TypeMirror type);
   
   public String getNextVarName();
   
   boolean isTypeOfComparable(TypeMirror type);
   
   boolean isBooleanType(TypeMirror type);
   
   boolean isNumberType(final TypeMirror type);
   
   ICodeGenerationContext getContext();
   
}