package com.wxxr.el.codegen;
/*
 * @(#)TestDomainResolver.java	 May 11, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */



import static java.util.Locale.ENGLISH;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import com.wxxr.javax.el.ELException;
import com.wxxr.mobile.core.util.StringUtils;

public class MockDomainResolver implements JavaDomainResolver {
  public int cnt = 0;
   /**
    * Returns a String which capitalizes the first letter of the string.
    */
   public static String capitalize(String name) { 
   if (name == null || name.length() == 0) { 
       return name; 
       }
   return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
   }

   public Class<?> resoveIdentifierType(String ident){
      if("repo".equals(ident)){
         return MockObject.class;
      }
      if("indexer".equals(ident)){
         return MessageIndexer.class;
      }
      return null;
   }
   
   
   public String generateGetIdentifierStatement(String ident){
      return new StringBuffer("((").append(resoveIdentifierType(ident).getCanonicalName()).append(")").append("context.getBean(\"").append(ident).append("\"))").toString();
   }

   public Class<?> resolvePropertyType(Class<?> source, String propertyName){
      try {
         if(source.isArray()&&"length".equals(propertyName)){
            return Integer.TYPE;
         }
         PropertyDescriptor pDesc = null;
         try {
            pDesc = new PropertyDescriptor(propertyName, source,"get"+capitalize(propertyName),null);
         }catch(IntrospectionException e){
            pDesc = new PropertyDescriptor(propertyName, source,"is"+capitalize(propertyName),null);
         }
         return pDesc.getPropertyType();
      }catch(Exception e){
         throw new ELException("Cannot find getter method of property["+propertyName+"]@"+source.getCanonicalName(), e);
      }
   }

   public String generateGetPropertyStatement(Class<?> source, String propertyName){
      try {
         if(source.isArray()&&"length".equals(propertyName)){
            return "length";
         }
         PropertyDescriptor pDesc = null;
         try {
            pDesc = new PropertyDescriptor(propertyName, source,"get"+capitalize(propertyName),null);
         }catch(IntrospectionException e){
            pDesc = new PropertyDescriptor(propertyName, source,"is"+capitalize(propertyName),null);
         }
         return new StringBuffer(pDesc.getReadMethod().getName()).append("()").toString();
      }catch(Exception e){
         throw new ELException("Cannot find getter method of property["+propertyName+"]@"+source.getCanonicalName(), e);
      }
   }

   public Class<?> resolveMethodReturnType(Class<?> source, String methodName,Class<?>[] paramTypes){
      try {
      Method m = source.getMethod(methodName, paramTypes);
      return m.getReturnType();
      }catch(Exception e){
         throw new ELException("Cannot find method :["+methodName+"("+((paramTypes != null)&&(paramTypes.length > 0) ?StringUtils.join(paramTypes,',') :"")+")]", e);
      }
   }
   
   public Class<?> resolveMultiplyResultType(Class<?> op1, Class<?> op2){
      if((op1 == Double.TYPE)||(op2 == Double.TYPE)){
         return Double.TYPE;
      }
      if((op1 == Long.TYPE)||(op2 == Long.TYPE)){
         return Long.TYPE;
      }
      if((op1 == Float.TYPE)||(op2 == Float.TYPE)){
         return Float.TYPE;
      }
      if((op1 == Integer.TYPE)||(op2 == Integer.TYPE)){
         return Integer.TYPE;
      }
      if((op1 == Short.TYPE)||(op2 == Short.TYPE)){
         return Short.TYPE;
      }
      return Byte.TYPE;
   }

   public String generateMethodCallStatement(Class<?> source, String methodName, String[] paramNames){
      return new StringBuffer(methodName).append('(').append((paramNames != null)&&(paramNames.length > 0) ? StringUtils.join(paramNames,',') : "").append(')').toString();
   }

   public String getDefaultValueOfType(Class<?> type) {
      if(!type.isPrimitive()){
         return "null";
      }else if(Number.class.isAssignableFrom(type)){
         return "0";
      }else if(type == Boolean.TYPE){
         return "false";
      }
      return "null";
   }

   public Class<?> resolveCollectionType(Class<?> clazz) {
      if(clazz.isArray()){
         return clazz.getComponentType();
      }
      return null;
   }
   
   
   public String getNextVarName() {
      return "__v"+cnt++;
   }


 

}
