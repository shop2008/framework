package com.wxxr.el.codegen;
/*
 * @(#)TestClosureDomainResolver.java	 May 16, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */




public class SimpleClosureDomainResolver implements JavaDomainResolver {

   final private JavaDomainResolver resolver;
   final private Class<?> identifierType;
   private String identifier;
   
   
   public SimpleClosureDomainResolver(JavaDomainResolver resolv,Class<?> inType){
      this.resolver = resolv;
      this.identifierType = inType;
   }

   /**
    * @param ident
    * @return
    * @see com.wxx.el.java.JavaDomainResolver#resoveIdentifierType(java.lang.String)
    */
   public Class<?> resoveIdentifierType(String ident) {
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
   public Class<?> resolvePropertyType(Class<?> source, String propertyName) {
      return resolver.resolvePropertyType(source, propertyName);
   }

   /**
    * @param source
    * @param propertyName
    * @return
    * @see com.wxx.el.java.JavaDomainResolver#generateGetPropertyStatement(java.lang.Class, java.lang.String)
    */
   public String generateGetPropertyStatement(Class<?> source,
         String propertyName) {
      return resolver.generateGetPropertyStatement(source, propertyName);
   }

   /**
    * @param source
    * @param methodName
    * @param paramTypes
    * @return
    * @see com.wxx.el.java.JavaDomainResolver#resolveMethodReturnType(java.lang.Class, java.lang.String, java.lang.Class<?>[])
    */
   public Class<?> resolveMethodReturnType(Class<?> source, String methodName,
         Class<?>[] paramTypes) {
      return resolver.resolveMethodReturnType(source, methodName, paramTypes);
   }

   /**
    * @param op1
    * @param op2
    * @return
    * @see com.wxx.el.java.JavaDomainResolver#resolveMultiplyResultType(java.lang.Class, java.lang.Class)
    */
   public Class<?> resolveMultiplyResultType(Class<?> op1, Class<?> op2) {
      return resolver.resolveMultiplyResultType(op1, op2);
   }

   /**
    * @param source
    * @param methodName
    * @param paramNames
    * @return
    * @see com.wxx.el.java.JavaDomainResolver#generateMethodCallStatement(java.lang.Class, java.lang.String, java.lang.String[])
    */
   public String generateMethodCallStatement(Class<?> source,
         String methodName, String[] paramNames) {
      return resolver.generateMethodCallStatement(source, methodName,
            paramNames);
   }

   /**
    * @param type
    * @return
    * @see com.wxx.el.java.JavaDomainResolver#getDefaultValueOfType(java.lang.Class)
    */
   public String getDefaultValueOfType(Class<?> type) {
      return resolver.getDefaultValueOfType(type);
   }

   /**
    * @param type
    * @return
    * @see com.wxx.el.java.JavaDomainResolver#resolveCollectionType(java.lang.Class)
    */
   public Class<?> resolveCollectionType(Class<?> type) {
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
}
