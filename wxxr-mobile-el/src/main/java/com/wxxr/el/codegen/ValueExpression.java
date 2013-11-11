/*
 * @(#)ValueExpression.java	 May 10, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.el.codegen;

import java.util.LinkedList;

public class ValueExpression {
   private LinkedList<ValueExpression> nestedVars = new LinkedList<ValueExpression>();
   private Class<?> type;
   private String javaStatement;
   private String varName;
   private boolean isContants = false,varAssignedInBody = false,semiColonRequired = true;
   private String constantValue;
   private ValueExpression wrapped = null;
   private boolean isWrapper = false;
   private String returnVarName;
   /**
    * Get the type.
    * 
    * @return the type.
    */
   public Class<?> getType() {
      if(isWrapper){
         return this.wrapped.getType();
      }
      return type;
   }
   /**
    * Get the javaStatement.
    * 
    * @return the javaStatement.
    */
   public String getJavaStatement() {
      if(isWrapper){
         return this.wrapped.getJavaStatement();
      }
      return javaStatement;
   }
   /**
    * Set the type.
    * 
    * @param type The type to set.
    */
   public void setType(Class<?> type) {
      if(isWrapper){
         this.wrapped.setType(type);
      }else{
         this.type = type;
      }
   }
   
   public boolean hasNestedValue() {
      if(isWrapper){
         return this.wrapped.hasNestedValue();
      }
      return this.nestedVars.size() > 0;
   }
   /**
    * Set the javaStatement.
    * 
    * @param javaStatement The javaStatement to set.
    */
   public void setJavaStatement(String javaStatement) {
      if(isWrapper){
         this.wrapped.setJavaStatement(javaStatement);
      }else{
         this.javaStatement = javaStatement;
      }
   }
   
   public void addNestedVar(ValueExpression var){
      if(isWrapper){
         this.wrapped.addNestedVar(var);
      }else{
         this.nestedVars.add(var);
      }
   }
   /**
    * Get the varName.
    * 
    * @return the varName.
    */
   public String getVarName() {
      if(isWrapper){
         return this.wrapped.getVarName();
      }else{
         return varName;
      }
   }
   /**
    * Set the varName.
    * 
    * @param varName The varName to set.
    */
   public void setVarName(String varName) {
      if(isWrapper){
         this.wrapped.setVarName(varName);
      }else{
         this.varName = varName;
      }
   }
   
   public void generateNestedValuesJavaCode(CompilingContext ctx,StringBuffer buf){
      if(isWrapper){
         this.wrapped.generateNestedValuesJavaCode(ctx, buf);
         return;
      }
      if(!this.nestedVars.isEmpty()){
         for (ValueExpression var : this.nestedVars) {
            var.generateJavaCode(ctx,buf);
            buf.append('\n');
         }
      }
   }

   
   public void generateJavaCode(CompilingContext ctx,StringBuffer buf){
      if(isWrapper){
         this.wrapped.generateJavaCode(ctx, buf);
         return;
      }
      generateNestedValuesJavaCode(ctx,buf);
      Class<?> returnType = getType();
      if((returnType != Void.class)&&(returnType != void.class)){
         if(this.varAssignedInBody){
            buf.append(getType().getCanonicalName()).append(' ').append(this.varName).append(" = ").append(ctx.getResolver().getDefaultValueOfType(getType())).append(";\n");
         }else{
            buf.append(getType().getCanonicalName()).append(' ').append(this.varName).append(" = ");
         }
      }
      buf.append(this.javaStatement);
      if(this.semiColonRequired){
         buf.append(';');
      }
   }
   /**
    * Get the isContants.
    * 
    * @return the isContants.
    */
   public boolean isContants() {
      if(isWrapper){
         return this.wrapped.isContants();
      }
      return isContants;
   }
   /**
    * Get the constantValue.
    * 
    * @return the constantValue.
    */
   public String getConstantValue() {
      if(isWrapper){
         return this.wrapped.getConstantValue();
      }
      return constantValue;
   }
   /**
    * Set the isContants.
    * 
    * @param isContants The isContants to set.
    */
   public void setContants(boolean isContants) {
      if(isWrapper){
         this.wrapped.setContants(isContants);
      }else{
         this.isContants = isContants;
      }
   }
   /**
    * Set the constantValue.
    * 
    * @param constantValue The constantValue to set.
    */
   public void setConstantValue(String constantValue) {
      if(isWrapper){
         this.wrapped.setConstantValue(constantValue);
      }else{
         this.constantValue = constantValue;
      }
   }
   /**
    * Get the varAssignedInBody.
    * 
    * @return the varAssignedInBody.
    */
   public boolean isVarAssignedInBody() {
      if(isWrapper){
         return this.wrapped.isVarAssignedInBody();
      }
      return varAssignedInBody;
   }
   /**
    * Get the semiColonRequired.
    * 
    * @return the semiColonRequired.
    */
   public boolean isSemiColonRequired() {
      if(isWrapper){
         this.wrapped.isSemiColonRequired();
      }
      return semiColonRequired;
   }
   /**
    * Set the varAssignedInBody.
    * 
    * @param varAssignedInBody The varAssignedInBody to set.
    */
   public void setVarAssignedInBody(boolean varAssignedInBody) {
      if(isWrapper){
         this.wrapped.setVarAssignedInBody(varAssignedInBody);
      }else{
         this.varAssignedInBody = varAssignedInBody;
      }
   }
   /**
    * Set the semiColonRequired.
    * 
    * @param semiColonRequired The semiColonRequired to set.
    */
   public void setSemiColonRequired(boolean semiColonRequired) {
      if(isWrapper){
         this.wrapped.setSemiColonRequired(semiColonRequired);
      }else{
         this.semiColonRequired = semiColonRequired;
      }
   }
   
   public void wrap(ValueExpression val){
      if(val == null){
         throw new IllegalArgumentException("Invalid wrapped value expression NULL !");
      }
      this.wrapped = val;
      this.isWrapper = true;
   }
   /**
    * Get the returnVarName.
    * 
    * @return the returnVarName.
    */
   public String getReturnVarName() {
      return (this.returnVarName != null) ? this.returnVarName : this.varName;
   }
   /**
    * Set the returnVarName.
    * 
    * @param returnVarName The returnVarName to set.
    */
   public void setReturnVarName(String returnVarName) {
      this.returnVarName = returnVarName;
   }
}
