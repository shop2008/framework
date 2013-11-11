/*
 * @(#)CompilingContext.java	 May 10, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.el.codegen;


public class CompilingContext  {
   private ValueExpression value;
   private JavaDomainResolver resolver;
   
   public final static boolean isNumberType(final Class<?> type) {
      return type == (java.lang.Long.class) || type == Long.TYPE || type == (java.lang.Double.class) || type == Double.TYPE || type == (java.lang.Byte.class) || type == Byte.TYPE || type == (java.lang.Short.class) || type == Short.TYPE || type == (java.lang.Integer.class) || type == Integer.TYPE || type == (java.lang.Float.class) || type == Float.TYPE || type == (java.math.BigInteger.class) || type == (java.math.BigDecimal.class);
  }

   
   public final static boolean isIdenticalType(final Class<?> type){
	   
	   return isNumberType(type) ||  type == Boolean.TYPE;
   }
   public String getNextVarName() {
      return this.resolver.getNextVarName();
   }

   /**
    * Get the value.
    * 
    * @return the value.
    */
   public ValueExpression getValue() {
      return value;
   }

   /**
    * Set the value.
    * 
    * @param value The value to set.
    */
   public CompilingContext setValue(ValueExpression value) {
      this.value = value;
      return this;
   }


   /**
    * Get the resolver.
    * 
    * @return the resolver.
    */
   public JavaDomainResolver getResolver() {
      return resolver;
   }


   /**
    * Set the resolver.
    * 
    * @param resolver The resolver to set.
    */
   public CompilingContext setResolver(JavaDomainResolver resolver) {
      this.resolver = resolver;
      return this;
   }
}
