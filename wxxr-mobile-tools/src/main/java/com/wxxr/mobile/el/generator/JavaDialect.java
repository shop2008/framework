/*
 * @(#)JavaVisitor.java	 May 10, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.el.generator;

import javax.lang.model.type.*;


import com.wxxr.mobile.core.tools.ICodeGenerationContext;
import com.wxxr.mobile.el.parser.AstAnd;
import com.wxxr.mobile.el.parser.AstBracketSuffix;
import com.wxxr.mobile.el.parser.AstChoice;
import com.wxxr.mobile.el.parser.AstClosure;
import com.wxxr.mobile.el.parser.AstClosureSuffix;
import com.wxxr.mobile.el.parser.AstCompositeExpression;
import com.wxxr.mobile.el.parser.AstDeferredExpression;
import com.wxxr.mobile.el.parser.AstDiv;
import com.wxxr.mobile.el.parser.AstDynamicExpression;
import com.wxxr.mobile.el.parser.AstEmpty;
import com.wxxr.mobile.el.parser.AstEqual;
import com.wxxr.mobile.el.parser.AstFalse;
import com.wxxr.mobile.el.parser.AstFloatingPoint;
import com.wxxr.mobile.el.parser.AstFunction;
import com.wxxr.mobile.el.parser.AstGreaterThan;
import com.wxxr.mobile.el.parser.AstGreaterThanEqual;
import com.wxxr.mobile.el.parser.AstIdentifier;
import com.wxxr.mobile.el.parser.AstInteger;
import com.wxxr.mobile.el.parser.AstLessThan;
import com.wxxr.mobile.el.parser.AstLessThanEqual;
import com.wxxr.mobile.el.parser.AstLiteralExpression;
import com.wxxr.mobile.el.parser.AstMethodSuffix;
import com.wxxr.mobile.el.parser.AstMinus;
import com.wxxr.mobile.el.parser.AstMod;
import com.wxxr.mobile.el.parser.AstMult;
import com.wxxr.mobile.el.parser.AstNegative;
import com.wxxr.mobile.el.parser.AstNot;
import com.wxxr.mobile.el.parser.AstNotEqual;
import com.wxxr.mobile.el.parser.AstNull;
import com.wxxr.mobile.el.parser.AstOr;
import com.wxxr.mobile.el.parser.AstPlus;
import com.wxxr.mobile.el.parser.AstPropertySuffix;
import com.wxxr.mobile.el.parser.AstString;
import com.wxxr.mobile.el.parser.AstTrue;
import com.wxxr.mobile.el.parser.AstValue;
import com.wxxr.mobile.el.parser.ELParserVisitor;
import com.wxxr.mobile.el.parser.Node;
import com.wxxr.mobile.el.parser.SimpleNode;

@SuppressWarnings("restriction")
public class JavaDialect implements ELParserVisitor {

	private final JavaDomainResolver resolver;
	
	public JavaDialect(JavaDomainResolver ctx){
		this.resolver = ctx;
	}
	
   public Object visit(SimpleNode node, Object data) {
      return null;
   }

   public Object visit(AstCompositeExpression node, Object data) {
      return node.childrenAccept(this, data);
   }

   public Object visit(AstLiteralExpression node, Object data) {
//      if(data != null){
//         CompilingContext ctx = (CompilingContext)data;
//         String s = node.getImage();
//         ValueExpression val = ctx.getValue();
//         val.setContants(true);
//         val.setConstantValue(s);
//         val.setType(String.class);
//      }
      return data;
   }

   public Object visit(AstDeferredExpression node, Object data) {
      return node.jjtGetChild(0).jjtAccept(this, data);
   }

   public Object visit(AstDynamicExpression node, Object data) {
      return node.jjtGetChild(0).jjtAccept(this, data);
   }
   
   public boolean isBooleanType(TypeMirror type){
	   return resolver.isBooleanType(type);
   }

   public Object visit(AstChoice node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression val = ctx.getValue();
      if(val == null){
         val = new ValueExpression();
         val.setVarName(ctx.getNextVarName());
         ctx.setValue(val);
      }
      ValueExpression cond = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      if(!isBooleanType(cond.getType())){
         throw new ELException("Invalid condition of choice :"+node.jjtGetChild(0));
      }
      ValueExpression ex1 = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      ValueExpression ex2 = visitNewValueNode( new Node[]{node.jjtGetChild(2)},ctx);
      if(!resolver.getContext().getProcessingEnvironment().getTypeUtils().isSameType(ex1.getType() ,ex2.getType())){
         throw new ELException("Invalid value type of choise ["+ex1.getType().toString()+" : "+ex2.getType().toString()+"]");
      }
      val.setType(ex1.getType());
      if(ex1.getVarName() == null){
         ex1.setVarName(ctx.getNextVarName());
      }
      if(ex2.getVarName() == null){
         ex2.setVarName(ctx.getNextVarName());
      }
      StringBuffer buf = new StringBuffer();
      if(val.getVarName() == null){
         val.setVarName(ctx.getNextVarName());
      }
      if(cond.isContants()){
         buf.append("if(").append(cond.getConstantValue()).append(") {\n");
      }else{
         cond.setVarName(ctx.getNextVarName());
         val.addNestedVar(cond);
         buf.append("if(").append(cond.getVarName()).append(") {\n");
      }
      if(ex1.isContants()){
         buf.append(val.getVarName()).append(" = ").append(ex1.getConstantValue()).append(";\n");
      }else{
         ex1.generateNestedValuesJavaCode(ctx,buf);
         buf.append(val.getVarName()).append(" = ").append(ex1.getJavaStatement()).append(";\n");
      }
      buf.append(" } else { \n");
      if(ex2.isContants()){
         buf.append(val.getVarName()).append(" = ").append(ex2.getConstantValue()).append(";\n");
      }else{
         ex2.generateNestedValuesJavaCode(ctx,buf);
         buf.append(val.getVarName()).append(" = ").append(ex2.getJavaStatement()).append(";\n");
      }
      buf.append(" } \n");
      val.setJavaStatement(buf.toString());
      val.setVarAssignedInBody(true);
      val.setSemiColonRequired(false);
      return ctx;
   }

   public Object visit(AstOr node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression val = ctx.getValue();
      if(val == null){
         val = new ValueExpression();
         val.setVarName(ctx.getNextVarName());
         ctx.setValue(val);
      }else if(val.getVarName() == null){
         val.setVarName(ctx.getNextVarName());
      }
      ValueExpression ex1 = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      if(!isBooleanType(ex1.getType())){
         throw new ELException("Invalid boolean value :"+node.jjtGetChild(0));
      }
      val.setType(ex1.getType());
      ValueExpression ex2 = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      if(!isBooleanType(ex2.getType())){
         throw new ELException("Invalid boolean value :"+node.jjtGetChild(1));
      }
      if(ex1.getVarName() == null){
         ex1.setVarName(ctx.getNextVarName());
      }
      if(ex2.getVarName() == null){
         ex2.setVarName(ctx.getNextVarName());
      }
      StringBuffer buf = new StringBuffer();
      if(ex1.isContants()){
         buf.append("if(").append(ex1.getConstantValue()).append(") {\n");
      }else{
         generateNestedCode(ctx, ex1, buf);
         buf.append("if(").append(ex1.getVarName()).append(") {\n");
      }
      buf.append(val.getVarName()).append(" = true; \n } else {\n");
      if(ex2.isContants()){
         buf.append(val.getVarName()).append(" = ").append(ex2.getConstantValue()).append("; \n");
      }else{
         generateNestedCode(ctx, ex2, buf);
         buf.append("if(").append(ex2.getVarName()).append(") {\n");
         buf.append(val.getVarName()).append(" = true; \n } else {\n");
         buf.append(val.getVarName()).append(" = false; \n }\n");
      }
      buf.append("}\n");
      val.setJavaStatement(buf.toString());
      val.setVarAssignedInBody(true);
      val.setSemiColonRequired(false);
      return ctx;
   }

   public Object visit(AstAnd node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression val = ctx.getValue();
      if(val == null){
         val = new ValueExpression();
         val.setVarName(ctx.getNextVarName());
         ctx.setValue(val);
      }else if(val.getVarName() == null){
         val.setVarName(ctx.getNextVarName());
      }
      ValueExpression ex1 = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      if(!isBooleanType(ex1.getType())){
         throw new ELException("Invalid boolean value :"+node.jjtGetChild(0));
      }
      val.setType(ex1.getType());
      ValueExpression ex2 = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      if(!isBooleanType(ex2.getType())){
         throw new ELException("Invalid boolean value :"+node.jjtGetChild(1));
      }
      if(ex1.getVarName() == null){
         ex1.setVarName(ctx.getNextVarName());
      }
      if(ex2.getVarName() == null){
         ex2.setVarName(ctx.getNextVarName());
      }
      StringBuffer buf = new StringBuffer();
      if(ex1.isContants()){
         buf.append("if(").append(ex1.getConstantValue()).append(" == false) {\n");
      }else{
         generateNestedCode(ctx, ex1, buf);
         buf.append("if(").append(ex1.getVarName()).append(" == false) {\n");
      }
      buf.append(val.getVarName()).append(" = false; \n } else {\n");
      if(ex2.isContants()){
         buf.append(val.getVarName()).append(" = ").append(ex2.getConstantValue()).append("; \n");
      }else{
         generateNestedCode(ctx, ex2, buf);
         buf.append("if(").append(ex2.getVarName()).append(") {\n");
         buf.append(val.getVarName()).append(" = true; \n } else {\n");
         buf.append(val.getVarName()).append(" = false; \n }\n");
      }
      buf.append("}\n");
      val.setJavaStatement(buf.toString());
      val.setVarAssignedInBody(true);
      val.setSemiColonRequired(false);
      return ctx;
   }
   
   public boolean isIdenticalType(final TypeMirror type){
	   
	   return isNumberType(type) ||  isBooleanType(type);
   }

   public boolean isTypeOfComparable(TypeMirror type){
	   return resolver.isTypeOfComparable(type);
   }
   
   public boolean isNumberType(final TypeMirror type) {
	   return resolver.isNumberType(type);
   }


   public Object visit(AstEqual node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression val = ctx.getValue();
      if(val == null){
         val = new ValueExpression();
         val.setVarName(ctx.getNextVarName());
         ctx.setValue(val);
      }
      ValueExpression ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      boolean isIdentical = isIdenticalType(ex.getType());
      if(ex.isContants()){
         val.setJavaStatement(ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(ex.getVarName());
      }
      val.setType(ex.getType());
      ex = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      val.setType(getBooleanPrimitiveType());
      if(isIdentical){
         if(ex.isContants()){
            val.setJavaStatement(val.getJavaStatement()+"=="+ex.getConstantValue());
         }else{
            ex.setVarName(ctx.getNextVarName());
            val.addNestedVar(ex);
            val.setJavaStatement(val.getJavaStatement()+"=="+ex.getVarName());
         }
      }else{
         if(ex.isContants()){
        	 if("null".equals(ex.getConstantValue())){
        		 val.setJavaStatement(val.getJavaStatement()+"=="+ex.getConstantValue());
        	 }else{
        		 val.setJavaStatement(val.getJavaStatement()+".equals("+ex.getConstantValue()+")");
        	 }
            
         }else{
            ex.setVarName(ctx.getNextVarName());
            val.addNestedVar(ex);
            val.setJavaStatement(val.getJavaStatement()+".equals("+ex.getVarName()+")");
         }
         
      }
      return ctx;
   }

	/**
	 * @return
	 */
	protected PrimitiveType getBooleanPrimitiveType() {
		return resolver.getContext().getProcessingEnvironment().getTypeUtils().getPrimitiveType(TypeKind.BOOLEAN);
	}

   public Object visit(AstNotEqual node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression val = ctx.getValue();
      if(val == null){
         val = new ValueExpression();
         val.setVarName(ctx.getNextVarName());
         ctx.setValue(val);
      }
      ValueExpression ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      boolean Identical = isIdenticalType(ex.getType());
      if(ex.isContants()){
         val.setJavaStatement(ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(ex.getVarName());
      }
      val.setType(ex.getType());
      ex = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      val.setType(getBooleanPrimitiveType());
      if(Identical){
         if(ex.isContants()){
            val.setJavaStatement(val.getJavaStatement()+"!="+ex.getConstantValue());
         }else{
            ex.setVarName(ctx.getNextVarName());
            val.addNestedVar(ex);
            val.setJavaStatement(val.getJavaStatement()+"!="+ex.getVarName());
         }
      }else{
         if(ex.isContants()){
        	 if("null".equals(ex.getConstantValue())){
        		 val.setJavaStatement(val.getJavaStatement()+"!="+ex.getConstantValue());
        	 }else{
        		 val.setJavaStatement("!"+val.getJavaStatement()+".equals("+ex.getConstantValue()+")");
        	 }
           
         }else{
            ex.setVarName(ctx.getNextVarName());
            val.addNestedVar(ex);
            val.setJavaStatement("!"+val.getJavaStatement()+".equals("+ex.getVarName()+")");
         }
         
      }
      return ctx;
   }

   public Object visit(AstLessThan node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression val = ctx.getValue();
      if(val == null){
         val = new ValueExpression();
         val.setVarName(ctx.getNextVarName());
         ctx.setValue(val);
      }
      ValueExpression ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      if((!isNumberType(ex.getType()))&&(!isTypeOfComparable(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(0));
      }
      boolean isNum = isNumberType(ex.getType());
      if(ex.isContants()){
         val.setJavaStatement(ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(ex.getVarName());
      }
      val.setType(ex.getType());
      ex = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      if(isNum &&(!isNumberType(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(1));
      }
      if((!isNum) &&(!isTypeOfComparable(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(1));
      }
      val.setType(getBooleanPrimitiveType());
      if(isNum){
         if(ex.isContants()){
            val.setJavaStatement(val.getJavaStatement()+"<"+ex.getConstantValue());
         }else{
            ex.setVarName(ctx.getNextVarName());
            val.addNestedVar(ex);
            val.setJavaStatement(val.getJavaStatement()+"<"+ex.getVarName());
         }
      }else{
         if(ex.isContants()){
            val.setJavaStatement(val.getJavaStatement()+".compareTo("+ex.getConstantValue()+") < 0");
         }else{
            ex.setVarName(ctx.getNextVarName());
            val.addNestedVar(ex);
            val.setJavaStatement(val.getJavaStatement()+".compareTo("+ex.getVarName()+") < 0");
         }
         
      }
      return ctx;
   }

   public Object visit(AstGreaterThan node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression val = ctx.getValue();
      if(val == null){
         val = new ValueExpression();
         val.setVarName(ctx.getNextVarName());
         ctx.setValue(val);
      }
      ValueExpression ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      if((!isNumberType(ex.getType()))&&(!isTypeOfComparable(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(0));
      }
      boolean isNum = isNumberType(ex.getType());
      if(ex.isContants()){
         val.setJavaStatement(ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(ex.getVarName());
      }
      val.setType(ex.getType());
      ex = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      if(isNum &&(!isNumberType(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(1));
      }
      if((!isNum) &&(!isTypeOfComparable(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(1));
      }
      val.setType(getBooleanPrimitiveType());
      if(isNum){
         if(ex.isContants()){
            val.setJavaStatement(val.getJavaStatement()+">"+ex.getConstantValue());
         }else{
            ex.setVarName(ctx.getNextVarName());
            val.addNestedVar(ex);
            val.setJavaStatement(val.getJavaStatement()+">"+ex.getVarName());
         }
      }else{
         if(ex.isContants()){
            val.setJavaStatement(val.getJavaStatement()+".compareTo("+ex.getConstantValue()+") > 0");
         }else{
            ex.setVarName(ctx.getNextVarName());
            val.addNestedVar(ex);
            val.setJavaStatement(val.getJavaStatement()+".compareTo("+ex.getVarName()+") > 0");
         }
         
      }
      return ctx;
   }

   public Object visit(AstLessThanEqual node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression val = ctx.getValue();
      if(val == null){
         val = new ValueExpression();
         val.setVarName(ctx.getNextVarName());
         ctx.setValue(val);
      }
      ValueExpression ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      if((!isNumberType(ex.getType()))&&(!isTypeOfComparable(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(0));
      }
      boolean isNum = isNumberType(ex.getType());
      if(ex.isContants()){
         val.setJavaStatement(ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(ex.getVarName());
      }
      val.setType(ex.getType());
      ex = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      if(isNum &&(!isNumberType(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(1));
      }
      if((!isNum) &&(!isTypeOfComparable(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(1));
      }
      val.setType(getBooleanPrimitiveType());
      if(isNum){
         if(ex.isContants()){
            val.setJavaStatement(val.getJavaStatement()+"<="+ex.getConstantValue());
         }else{
            ex.setVarName(ctx.getNextVarName());
            val.addNestedVar(ex);
            val.setJavaStatement(val.getJavaStatement()+"<="+ex.getVarName());
         }
      }else{
         if(ex.isContants()){
            val.setJavaStatement(val.getJavaStatement()+".compareTo("+ex.getConstantValue()+") <= 0");
         }else{
            ex.setVarName(ctx.getNextVarName());
            val.addNestedVar(ex);
            val.setJavaStatement(val.getJavaStatement()+".compareTo("+ex.getVarName()+") <= 0");
         }
         
      }
      return ctx;
   }

   public Object visit(AstGreaterThanEqual node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression val = ctx.getValue();
      if(val == null){
         val = new ValueExpression();
         val.setVarName(ctx.getNextVarName());
         ctx.setValue(val);
      }
      ValueExpression ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      if((!isNumberType(ex.getType()))&&(!isTypeOfComparable(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(0));
      }
      boolean isNum = isNumberType(ex.getType());
      if(ex.isContants()){
         val.setJavaStatement(ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(ex.getVarName());
      }
      val.setType(ex.getType());
      ex = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      if(isNum &&(!isNumberType(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(1));
      }
      if((!isNum) &&(!isTypeOfComparable(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(1));
      }
      val.setType(getBooleanPrimitiveType());
      if(isNum){
         if(ex.isContants()){
            val.setJavaStatement(val.getJavaStatement()+">="+ex.getConstantValue());
         }else{
            ex.setVarName(ctx.getNextVarName());
            val.addNestedVar(ex);
            val.setJavaStatement(val.getJavaStatement()+">="+ex.getVarName());
         }
      }else{
         if(ex.isContants()){
            val.setJavaStatement(val.getJavaStatement()+".compareTo("+ex.getConstantValue()+") >= 0");
         }else{
            ex.setVarName(ctx.getNextVarName());
            val.addNestedVar(ex);
            val.setJavaStatement(val.getJavaStatement()+".compareTo("+ex.getVarName()+") >= 0");
         }
         
      }
      return ctx;
   }

   public Object visit(AstPlus node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      if(!isNumberType(ex.getType())){
         throw new ELException("operand is not number of (+) :"+node.jjtGetChild(0));
      }
      if(ex.isContants()){
         ex.setConstantValue("+"+ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         ex.setJavaStatement("+("+ex.getJavaStatement()+")");
      }
      ValueExpression val = ctx.getValue();
      if(val == null){
         ctx.setValue(ex);
      }else{
         val.wrap(ex);
      }
      return ctx;
   }

   public Object visit(AstMinus node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      if(!isNumberType(ex.getType())){
         throw new ELException("operand is not number of (+) :"+node.jjtGetChild(0));
      }
      if(ex.isContants()){
         ex.setConstantValue("-"+ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         ex.setJavaStatement("-("+ex.getJavaStatement()+")");
      }
      ValueExpression val = ctx.getValue();
      if(val == null){
         ctx.setValue(ex);
      }else{
         val.wrap(ex);
      }
      return ctx;
   }

   public Object visit(AstMult node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression val = ctx.getValue();
      if(val == null){
         val = new ValueExpression();
         val.setVarName(ctx.getNextVarName());
         ctx.setValue(val);
      }
      ValueExpression ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      JavaDomainResolver resolver = ctx.getResolver();
      if(!isNumberType(ex.getType())){
         throw new ELException("Invalid multiply number :"+ex.getConstantValue());
      }
      if(ex.isContants()){
         val.setJavaStatement(ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(ex.getVarName());
      }
      val.setType(ex.getType());
      ex = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      if(!isNumberType(ex.getType())){
         throw new ELException("Invalid multiply number :"+ex.getConstantValue());
      }
      val.setType(resolver.resolveMultiplyResultType(val.getType(), ex.getType()));
      if(ex.isContants()){
         val.setJavaStatement(val.getJavaStatement()+"*"+ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(val.getJavaStatement()+"*"+ex.getVarName());
      }
      return ctx;
   }

   public Object visit(AstDiv node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      JavaDomainResolver resolver = ctx.getResolver();
      ValueExpression val = ctx.getValue();
      if(val == null){
         val = new ValueExpression();
         val.setVarName(ctx.getNextVarName());
         ctx.setValue(val);
      }
      ValueExpression ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      if(!isNumberType(ex.getType())){
         throw new ELException("Invalid multiply number :"+ex.getConstantValue());
      }
      if(ex.isContants()){
         val.setJavaStatement(ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(ex.getVarName());
      }
      val.setType(ex.getType());
      ex = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      if(!isNumberType(ex.getType())){
         throw new ELException("Invalid multiply number :"+ex.getConstantValue());
      }
      val.setType(resolver.resolveMultiplyResultType(val.getType(), ex.getType()));
      if(ex.isContants()){
         val.setJavaStatement(val.getJavaStatement()+"/"+ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(val.getJavaStatement()+"/"+ex.getVarName());
      }
      return ctx;
   }

   public Object visit(AstMod node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression val = ctx.getValue();
      if(val == null){
         val = new ValueExpression();
         val.setVarName(ctx.getNextVarName());
         ctx.setValue(val);
      }
      ValueExpression ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      if(!isNumberType(ex.getType())){
         throw new ELException("Invalid multiply number :"+ex.getConstantValue());
      }
      if(ex.isContants()){
         val.setJavaStatement(ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(ex.getVarName());
      }
      ex = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      if(!isNumberType(ex.getType())){
         throw new ELException("Invalid multiply number :"+ex.getConstantValue());
      }
      val.setType(resolver.getContext().getProcessingEnvironment().getTypeUtils().getPrimitiveType(TypeKind.INT));
      if(ex.isContants()){
         val.setJavaStatement(val.getJavaStatement()+"%"+ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(val.getJavaStatement()+"%"+ex.getVarName());
      }
      return ctx;
   }

   public Object visit(AstNegative node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression val = ctx.getValue();
      if(val == null){
         val = new ValueExpression();
         val.setVarName(ctx.getNextVarName());
         ctx.setValue(val);
      }
      ValueExpression ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      if(!isNumberType(ex.getType())){
         throw new ELException("Invalid multiply number :"+ex.getConstantValue());
      }
      if(ex.isContants()){
         val.setJavaStatement("-"+ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement("-"+ex.getVarName());
      }
      val.setType(ex.getType());
      return ctx;
   }

   public Object visit(AstNot node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression val = ctx.getValue();
      if(val == null){
         val = new ValueExpression();
         val.setVarName(ctx.getNextVarName());
         ctx.setValue(val);
      }else if(val.getVarName() == null){
         val.setVarName(ctx.getNextVarName());
      }
      ValueExpression ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      if(!isBooleanType(ex.getType())){
         throw new ELException("Invalid boolean value :"+node.jjtGetChild(0));
      }
      if(ex.getVarName() == null){
         ex.setVarName(ctx.getNextVarName());
      }
      if(ex.isContants()){
         val.setConstantValue("!"+ex.getConstantValue());
      }else{
         StringBuffer buf = new StringBuffer();
         generateNestedCode(ctx, ex, buf);
         buf.append(val.getVarName()).append(" = !").append(ex.getVarName()).append(";\n");
         val.setJavaStatement(buf.toString());
         val.setVarAssignedInBody(true);
         val.setSemiColonRequired(false);
      }
      val.setType(getBooleanPrimitiveType());
      return ctx;
   }

   public Object visit(AstEmpty node, Object data) {
      return data;
   }

   public Object visit(AstValue node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression val = ctx.getValue();
      if(val == null){
         val = new ValueExpression();
         val.setVarName(ctx.getNextVarName());
         ctx.setValue(val);
      }
      node.childrenAccept(this, ctx);
      return ctx;
   }

   /** 
    * method desc��
    * @param child
    * @param ctx
    * @param data
    */
   protected ValueExpression visitNewValueNode(Node[] nodes, CompilingContext ctx) {
      ValueExpression val = new ValueExpression();
      ValueExpression old = null;
      if(ctx.getValue() == null){
         ctx.setValue(val);
      }else{
         old = ctx.getValue();
         ctx.setValue(val);
      }
      try {
         for (Node n : nodes) {
            n.jjtAccept(this, ctx);
         }
      }finally {
         ctx.setValue(old);
      }
      return val;
   }

   public Object visit(AstPropertySuffix node, Object data) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         String s = node.getImage();
         ValueExpression val = ctx.getValue();
         TypeMirror type = val.getType();
         val.setType(resolver.resolvePropertyType(type.toString(), s));
         val.setJavaStatement(val.getJavaStatement()+"."+resolver.generateGetPropertyStatement(type.toString(), s));
      }
      return data;
   }

   public Object visit(AstBracketSuffix node, Object data) {
      // FIXME visit
      return null;
   }

   public Object visit(AstMethodSuffix node, Object data) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         JavaDomainResolver resolver = ctx.getResolver();
         String s = node.getImage();
         ValueExpression val = ctx.getValue();
         TypeMirror paramTypes[] = null;
         String[] args = null;
         TypeMirror type = val.getType();
         if((node.jjtGetNumChildren() > 0)){
            int cnt = node.jjtGetNumChildren();
            paramTypes = new TypeMirror[cnt];
            args = new String[cnt];
            for (int i = 0; i < cnt; i++) {
               Node n = node.jjtGetChild(i);               
               ValueExpression ex = visitNewValueNode(new Node[]{n}, ctx);
                if(ex.isContants()){
                   paramTypes[i]=ex.getType();
                   args[i] = ex.getConstantValue();
                }else{
                   ex.setVarName(ctx.getNextVarName());
                   val.addNestedVar(ex);
                   paramTypes[i]=ex.getType();
                   args[i] = ex.getVarName();
                }
            }
         }
         val.setType(resolver.resolveMethodReturnType(type.toString(), s, paramTypes));
         val.setJavaStatement(val.getJavaStatement()+"."+resolver.generateMethodCallStatement(type.toString(), s, args));
      }
      return data;
   }

   public Object visit(AstClosureSuffix node, Object data) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      
      ValueExpression val = ctx.getValue();
      if(val == null){
         throw new ELException("Invalid closure !!!");
      }
      TypeMirror rawType = ctx.getResolver().resolveCollectionType(val.getType());
      if(rawType == null){
         throw new ELException("Currently Closure is  only avaiable for Array and Parameterized Collection !!!");
      }
      JavaDomainResolver oldResolver = ctx.getResolver();
      SimpleClosureDomainResolver cResolver = new SimpleClosureDomainResolver(oldResolver, rawType);
      ctx.setResolver(cResolver);
      ValueExpression ex = null;
      try {
         ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      } finally {
         ctx.setResolver(oldResolver);
      }
      StringBuffer buf = new StringBuffer();
      String prevVarName = val.getVarName();
      if(val.isVarAssignedInBody()){
         buf.append(val.getType().toString()).append(" ").append(val.getVarName()).append(" = ").append(ctx.getResolver().getDefaultValueOfType(val.getType())).append(";\n");
      }else{
         buf.append(val.getType().toString()).append(" ").append(val.getVarName()).append(" = ");
      }
      buf.append(val.getJavaStatement()).append(";\n");
      val.setVarName(ctx.getNextVarName());
      String arrayName = val.getVarName();
      buf.append("java.util.LinkedList ").append(arrayName).append(" = ").append("new java.util.LinkedList();\n");
      buf.append("foreach( ").append(rawType.toString()).append(" ").append(cResolver.getIdentifier()).append(" in ").append(prevVarName).append(" ) {\n");
      if(ex.getVarName() == null){
         ex.setVarName(ctx.getNextVarName());
      }
      generateNestedCode(ctx, ex, buf);
      buf.append(arrayName).append(".add(").append(ex.getVarName()).append(");\n");
      buf.append("}\n");
      val.setVarName(ctx.getNextVarName());
      val.setType(resolver.getContext().getProcessingEnvironment().getTypeUtils().getArrayType(ex.getType()));
      buf.append(val.getVarName()).append(" = (").append(val.getType().toString()).append(')').append(arrayName).
      append(".toArray( new ").append(ex.getType().toString()).
      append("[").append(arrayName).append(".size()]);");
      val.setJavaStatement(buf.toString());
      val.setVarAssignedInBody(true);
      val.setSemiColonRequired(false);
      return ctx;
   }

   /** 
    * method desc��
    * @param ctx
    * @param ex
    * @param buf
    */
   protected void generateNestedCode(CompilingContext ctx, ValueExpression ex,
         StringBuffer buf) {
      ex.generateNestedValuesJavaCode(ctx, buf);
      if(ex.isVarAssignedInBody()){
         buf.append(ex.getType().toString()).append(" ").append(ex.getVarName()).append(" = ").append(ctx.getResolver().getDefaultValueOfType(ex.getType())).append(";\n");
      }else{
         buf.append(ex.getType().toString()).append(" ").append(ex.getVarName()).append(" = ");
      }
      buf.append(ex.getJavaStatement());
      if(ex.isSemiColonRequired()){
         buf.append(";");
      }
      buf.append('\n');
   }

   public Object visit(AstClosure node, Object data) {
      return node.childrenAccept(this, data);
   }

   public Object visit(AstIdentifier node, Object data) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         JavaDomainResolver resolver = ctx.getResolver();
         String s = node.getImage();
         ValueExpression val = ctx.getValue();
         val.setType(resolver.resoveIdentifierType(s));
         val.setJavaStatement(resolver.generateGetIdentifierStatement(s));
      }
      return data;
   }

   public Object visit(AstFunction node, Object data) {
      // FIXME visit
      return null;
   }

   public Object visit(AstTrue node, Object data) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         ValueExpression val = ctx.getValue();
         val.setContants(true);
         val.setConstantValue("true");
         val.setType(getBooleanPrimitiveType());
      }
      return data;
   }

   public Object visit(AstFalse node, Object data) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         ValueExpression val = ctx.getValue();
         val.setContants(true);
         val.setConstantValue("false");
         val.setType(getBooleanPrimitiveType());
      }
      return data;
   }

   public Object visit(AstFloatingPoint node, Object data) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         String s = node.getImage();
         ValueExpression val = ctx.getValue();
         val.setContants(true);
         val.setConstantValue(s);
         val.setType(resolver.getContext().getProcessingEnvironment().getTypeUtils().getPrimitiveType(TypeKind.FLOAT));
      }
      return data;
   }

   public Object visit(AstInteger node, Object data) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         String s = node.getImage();
         ValueExpression val = ctx.getValue();
         val.setContants(true);
         val.setConstantValue(s);
         val.setType(resolver.getContext().getProcessingEnvironment().getTypeUtils().getPrimitiveType(TypeKind.INT));
      }
      return data;
   }

   public Object visit(AstString node, Object data) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         String s = node.getImage();
         ValueExpression val = ctx.getValue();
         val.setContants(true);
         val.setConstantValue(s);
         val.setType(resolver.getContext().getProcessingEnvironment().getElementUtils().getTypeElement(String.class.getCanonicalName()).asType());
      }
      return data;
   }

   public Object visit(AstNull node, Object data) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         ValueExpression val = ctx.getValue();
         val.setContants(true);
         val.setConstantValue("null");
         val.setType(null);
      }
      return data;
   }

}
