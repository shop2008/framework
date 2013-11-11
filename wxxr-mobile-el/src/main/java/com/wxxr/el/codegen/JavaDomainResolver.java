/*
 * @(#)JavaDomainResolver.java May 11, 2011 Copyright 2004-2011 WXXR Network
 * Technology Co. Ltd. All rights reserved. WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.el.codegen;

public interface JavaDomainResolver {

   public Class<?> resoveIdentifierType(String ident);

   public String generateGetIdentifierStatement(String ident);

   public Class<?> resolvePropertyType(Class<?> source, String propertyName);

   public String generateGetPropertyStatement(Class<?> source,
         String propertyName);

   public Class<?> resolveMethodReturnType(Class<?> source, String methodName,
         Class<?>[] paramTypes);

   public Class<?> resolveMultiplyResultType(Class<?> op1, Class<?> op2);

   public String generateMethodCallStatement(Class<?> source,
         String methodName, String[] paramNames);

   public String getDefaultValueOfType(Class<?> type);
   
   public Class<?> resolveCollectionType(Class<?> type);
   
   public String getNextVarName();
}