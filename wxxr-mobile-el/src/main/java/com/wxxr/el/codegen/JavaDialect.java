/*
 * @(#)JavaVisitor.java	 May 10, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.el.codegen;

import java.lang.reflect.Array;

import com.sun.el.parser.AstAnd;
import com.sun.el.parser.AstAssign;
import com.sun.el.parser.AstBracketSuffix;
import com.sun.el.parser.AstChoice;
import com.sun.el.parser.AstCompositeExpression;
import com.sun.el.parser.AstConcat;
import com.sun.el.parser.AstDeferredExpression;
import com.sun.el.parser.AstDiv;
import com.sun.el.parser.AstDotSuffix;
import com.sun.el.parser.AstDynamicExpression;
import com.sun.el.parser.AstEmpty;
import com.sun.el.parser.AstEqual;
import com.sun.el.parser.AstFalse;
import com.sun.el.parser.AstFloatingPoint;
import com.sun.el.parser.AstFunction;
import com.sun.el.parser.AstGreaterThan;
import com.sun.el.parser.AstGreaterThanEqual;
import com.sun.el.parser.AstIdentifier;
import com.sun.el.parser.AstInteger;
import com.sun.el.parser.AstLambdaExpression;
import com.sun.el.parser.AstLambdaParameters;
import com.sun.el.parser.AstLessThan;
import com.sun.el.parser.AstLessThanEqual;
import com.sun.el.parser.AstListData;
import com.sun.el.parser.AstLiteralExpression;
import com.sun.el.parser.AstMapData;
import com.sun.el.parser.AstMapEntry;
import com.sun.el.parser.AstMethodArguments;
import com.sun.el.parser.AstMinus;
import com.sun.el.parser.AstMod;
import com.sun.el.parser.AstMult;
import com.sun.el.parser.AstNegative;
import com.sun.el.parser.AstNot;
import com.sun.el.parser.AstNotEqual;
import com.sun.el.parser.AstNull;
import com.sun.el.parser.AstOr;
import com.sun.el.parser.AstPlus;
import com.sun.el.parser.AstPropertySuffix;
import com.sun.el.parser.AstString;
import com.sun.el.parser.AstTrue;
import com.sun.el.parser.AstValue;
import com.sun.el.parser.Node;
import com.sun.el.parser.NodeVisitor;
import com.sun.el.parser.SimpleNode;
import com.wxxr.javax.el.ELException;

public class JavaDialect implements NodeVisitor {

	private CompilingContext data;
	
	public JavaDialect(){}
	
	public JavaDialect(CompilingContext ctx){
		this.data = ctx;
	}
   public void visit(SimpleNode node) {
   }

   public void visit(AstCompositeExpression node) {
      node.visitChildren(this);
   }

   public void visit(AstLiteralExpression node) {
//      if(data != null){
//         CompilingContext ctx = (CompilingContext)data;
//         String s = node.getImage();
//         ValueExpression val = ctx.getValue();
//         val.setContants(true);
//         val.setConstantValue(s);
//         val.setType(String.class);
//      }
   }

   public void visit(AstDeferredExpression node) {
      node.jjtGetChild(0).accept(this);
   }

   public void visit(AstDynamicExpression node) {
      node.jjtGetChild(0).accept(this);
   }

   public void visit(AstChoice node) {
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
      if(cond.getType() != Boolean.TYPE){
         throw new ELException("Invalid condition of choice :"+node.jjtGetChild(0));
      }
      ValueExpression ex1 = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      ValueExpression ex2 = visitNewValueNode( new Node[]{node.jjtGetChild(2)},ctx);
      if(ex1.getType() != ex2.getType()){
         throw new ELException("Invalid value type of choise ["+ex1.getType().getCanonicalName()+" : "+ex2.getType().getCanonicalName()+"]");
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
   }

   public void visit(AstOr node) {
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
      if(ex1.getType() != Boolean.TYPE){
         throw new ELException("Invalid boolean value :"+node.jjtGetChild(0));
      }
      val.setType(ex1.getType());
      ValueExpression ex2 = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      if(ex2.getType() != Boolean.TYPE){
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
   }

   public void visit(AstAnd node) {
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
      if(ex1.getType() != Boolean.TYPE){
         throw new ELException("Invalid boolean value :"+node.jjtGetChild(0));
      }
      val.setType(ex1.getType());
      ValueExpression ex2 = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      if(ex2.getType() != Boolean.TYPE){
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
   }

   public void visit(AstEqual node) {
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
      boolean isIdentical = CompilingContext.isIdenticalType(ex.getType());
      if(ex.isContants()){
         val.setJavaStatement(ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(ex.getVarName());
      }
      val.setType(ex.getType());
      ex = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      val.setType(Boolean.TYPE);
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
   }

   public void visit(AstNotEqual node) {
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
      boolean Identical = CompilingContext.isIdenticalType(ex.getType());
      if(ex.isContants()){
         val.setJavaStatement(ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(ex.getVarName());
      }
      val.setType(ex.getType());
      ex = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      val.setType(Boolean.TYPE);
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
   }

   public void visit(AstLessThan node) {
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
      if((!CompilingContext.isNumberType(ex.getType()))&&(!Comparable.class.isAssignableFrom(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(0));
      }
      boolean isNum = CompilingContext.isNumberType(ex.getType());
      if(ex.isContants()){
         val.setJavaStatement(ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(ex.getVarName());
      }
      val.setType(ex.getType());
      ex = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      if(isNum &&(!CompilingContext.isNumberType(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(1));
      }
      if((!isNum) &&(!Comparable.class.isAssignableFrom(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(1));
      }
      val.setType(Boolean.TYPE);
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
   }

   public void visit(AstGreaterThan node) {
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
      if((!CompilingContext.isNumberType(ex.getType()))&&(!Comparable.class.isAssignableFrom(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(0));
      }
      boolean isNum = CompilingContext.isNumberType(ex.getType());
      if(ex.isContants()){
         val.setJavaStatement(ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(ex.getVarName());
      }
      val.setType(ex.getType());
      ex = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      if(isNum &&(!CompilingContext.isNumberType(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(1));
      }
      if((!isNum) &&(!Comparable.class.isAssignableFrom(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(1));
      }
      val.setType(Boolean.TYPE);
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
   }

   public void visit(AstLessThanEqual node) {
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
      if((!CompilingContext.isNumberType(ex.getType()))&&(!Comparable.class.isAssignableFrom(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(0));
      }
      boolean isNum = CompilingContext.isNumberType(ex.getType());
      if(ex.isContants()){
         val.setJavaStatement(ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(ex.getVarName());
      }
      val.setType(ex.getType());
      ex = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      if(isNum &&(!CompilingContext.isNumberType(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(1));
      }
      if((!isNum) &&(!Comparable.class.isAssignableFrom(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(1));
      }
      val.setType(Boolean.TYPE);
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
   }

   public void visit(AstGreaterThanEqual node) {
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
      if((!CompilingContext.isNumberType(ex.getType()))&&(!Comparable.class.isAssignableFrom(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(0));
      }
      boolean isNum = CompilingContext.isNumberType(ex.getType());
      if(ex.isContants()){
         val.setJavaStatement(ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(ex.getVarName());
      }
      val.setType(ex.getType());
      ex = visitNewValueNode( new Node[]{node.jjtGetChild(1)},ctx);
      if(isNum &&(!CompilingContext.isNumberType(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(1));
      }
      if((!isNum) &&(!Comparable.class.isAssignableFrom(ex.getType()))){
         throw new ELException("Invalid lessthan operand :"+node.jjtGetChild(1));
      }
      val.setType(Boolean.TYPE);
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
   }

   public void visit(AstPlus node) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      if(!CompilingContext.isNumberType(ex.getType())){
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
   }

   public void visit(AstMinus node) {
      CompilingContext ctx = null;
      if(data == null){
         ctx = new CompilingContext();
      }else{
         ctx = (CompilingContext)data;
      }
      ValueExpression ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
      if(!CompilingContext.isNumberType(ex.getType())){
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
   }

   public void visit(AstMult node) {
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
      if(!CompilingContext.isNumberType(ex.getType())){
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
      if(!CompilingContext.isNumberType(ex.getType())){
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
   }

   public void visit(AstDiv node) {
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
      if(!CompilingContext.isNumberType(ex.getType())){
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
      if(!CompilingContext.isNumberType(ex.getType())){
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
   }

   public void visit(AstMod node) {
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
      if(!CompilingContext.isNumberType(ex.getType())){
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
      if(!CompilingContext.isNumberType(ex.getType())){
         throw new ELException("Invalid multiply number :"+ex.getConstantValue());
      }
      val.setType(Integer.TYPE);
      if(ex.isContants()){
         val.setJavaStatement(val.getJavaStatement()+"%"+ex.getConstantValue());
      }else{
         ex.setVarName(ctx.getNextVarName());
         val.addNestedVar(ex);
         val.setJavaStatement(val.getJavaStatement()+"%"+ex.getVarName());
      }
   }

   public void visit(AstNegative node) {
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
      if(!CompilingContext.isNumberType(ex.getType())){
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
   }

   public void visit(AstNot node) {
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
      if(ex.getType() != Boolean.TYPE){
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
      val.setType(Boolean.TYPE);
   }

   public void visit(AstEmpty node) {
   }

   public void visit(AstValue node) {
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
      node.visitChildren(this);
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
            n.accept(this);
         }
      }finally {
         ctx.setValue(old);
      }
      return val;
   }

   public void visit(AstPropertySuffix node) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         JavaDomainResolver resolver = ctx.getResolver();
         String s = node.getImage();
         ValueExpression val = ctx.getValue();
         Class<?> type = val.getType();
         val.setType(resolver.resolvePropertyType(type, s));
         val.setJavaStatement(val.getJavaStatement()+"."+resolver.generateGetPropertyStatement(type, s));
      }
   }

   public void visit(AstBracketSuffix node) {
   }

//   public void visit(AstMethodSuffix node) {
//      if(data != null){
//         CompilingContext ctx = (CompilingContext)data;
//         JavaDomainResolver resolver = ctx.getResolver();
//         String s = node.getImage();
//         ValueExpression val = ctx.getValue();
//         Class<?> paramTypes[] = null;
//         String[] args = null;
//         Class<?> type = val.getType();
//         if((node.jjtGetNumChildren() > 0)){
//            int cnt = node.jjtGetNumChildren();
//            paramTypes = new Class<?>[cnt];
//            args = new String[cnt];
//            for (int i = 0; i < cnt; i++) {
//               Node n = node.jjtGetChild(i);               
//               ValueExpression ex = visitNewValueNode(new Node[]{n}, ctx);
//                if(ex.isContants()){
//                   paramTypes[i]=ex.getType();
//                   args[i] = ex.getConstantValue();
//                }else{
//                   ex.setVarName(ctx.getNextVarName());
//                   val.addNestedVar(ex);
//                   paramTypes[i]=ex.getType();
//                   args[i] = ex.getVarName();
//                }
//            }
//         }
//         val.setType(resolver.resolveMethodReturnType(type, s, paramTypes));
//         val.setJavaStatement(val.getJavaStatement()+"."+resolver.generateMethodCallStatement(type, s, args));
//      }
//   }

//   public void visit(AstClosureSuffix node) {
//      CompilingContext ctx = null;
//      if(data == null){
//         ctx = new CompilingContext();
//      }else{
//         ctx = (CompilingContext)data;
//      }
//      
//      ValueExpression val = ctx.getValue();
//      if(val == null){
//         throw new ELException("Invalid closure !!!");
//      }
//      Class<?> rawType = ctx.getResolver().resolveCollectionType(val.getType());
//      if(rawType == null){
//         throw new ELException("Currently Closure is  only avaiable for Array and Parameterized Collection !!!");
//      }
//      JavaDomainResolver oldResolver = ctx.getResolver();
//      SimpleClosureDomainResolver cResolver = new SimpleClosureDomainResolver(oldResolver, rawType);
//      ctx.setResolver(cResolver);
//      ValueExpression ex = null;
//      try {
//         ex = visitNewValueNode( new Node[]{node.jjtGetChild(0)},ctx);
//      } finally {
//         ctx.setResolver(oldResolver);
//      }
//      StringBuffer buf = new StringBuffer();
//      String prevVarName = val.getVarName();
//      if(val.isVarAssignedInBody()){
//         buf.append(val.getType().getCanonicalName()).append(" ").append(val.getVarName()).append(" = ").append(ctx.getResolver().getDefaultValueOfType(val.getType())).append(";\n");
//      }else{
//         buf.append(val.getType().getCanonicalName()).append(" ").append(val.getVarName()).append(" = ");
//      }
//      buf.append(val.getJavaStatement()).append(";\n");
//      val.setVarName(ctx.getNextVarName());
//      String arrayName = val.getVarName();
//      buf.append("java.util.LinkedList ").append(arrayName).append(" = ").append("new java.util.LinkedList();\n");
//      buf.append("foreach( ").append(rawType.getCanonicalName()).append(" ").append(cResolver.getIdentifier()).append(" in ").append(prevVarName).append(" ) {\n");
//      if(ex.getVarName() == null){
//         ex.setVarName(ctx.getNextVarName());
//      }
//      generateNestedCode(ctx, ex, buf);
//      buf.append(arrayName).append(".add(").append(ex.getVarName()).append(");\n");
//      buf.append("}\n");
//      val.setVarName(ctx.getNextVarName());
//      val.setType(Array.newInstance(ex.getType(),0).getClass());
//      buf.append(val.getVarName()).append(" = (").append(val.getType().getCanonicalName()).append(')').append(arrayName).
//      append(".toArray( new ").append(ex.getType().getCanonicalName()).
//      append("[").append(arrayName).append(".size()]);");
//      val.setJavaStatement(buf.toString());
//      val.setVarAssignedInBody(true);
//      val.setSemiColonRequired(false);
//   }

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
         buf.append(ex.getType().getCanonicalName()).append(" ").append(ex.getVarName()).append(" = ").append(ctx.getResolver().getDefaultValueOfType(ex.getType())).append(";\n");
      }else{
         buf.append(ex.getType().getCanonicalName()).append(" ").append(ex.getVarName()).append(" = ");
      }
      buf.append(ex.getJavaStatement());
      if(ex.isSemiColonRequired()){
         buf.append(";");
      }
      buf.append('\n');
   }

//   public Object visit(AstClosure node) {
//      return node.accept(this);
//   }

   public void visit(AstIdentifier node) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         JavaDomainResolver resolver = ctx.getResolver();
         String s = node.getImage();
         ValueExpression val = ctx.getValue();
         val.setType(resolver.resoveIdentifierType(s));
         val.setJavaStatement(resolver.generateGetIdentifierStatement(s));
      }
   }

   public void visit(AstFunction node) {
   }

   public void visit(AstTrue node) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         ValueExpression val = ctx.getValue();
         val.setContants(true);
         val.setConstantValue("true");
         val.setType(Boolean.TYPE);
      }
   }

   public void visit(AstFalse node) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         ValueExpression val = ctx.getValue();
         val.setContants(true);
         val.setConstantValue("false");
         val.setType(Boolean.TYPE);
      }
   }

   public void visit(AstFloatingPoint node) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         String s = node.getImage();
         ValueExpression val = ctx.getValue();
         val.setContants(true);
         val.setConstantValue(s);
         val.setType(Float.TYPE);
      }
   }

   public void visit(AstInteger node) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         String s = node.getImage();
         ValueExpression val = ctx.getValue();
         val.setContants(true);
         val.setConstantValue(s);
         val.setType(Integer.TYPE);
      }
   }

   public void visit(AstString node) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         String s = node.getImage();
         ValueExpression val = ctx.getValue();
         val.setContants(true);
         val.setConstantValue(s);
         val.setType(String.class);
      }
   }

   public void visit(AstNull node) {
      if(data != null){
         CompilingContext ctx = (CompilingContext)data;
         ValueExpression val = ctx.getValue();
         val.setContants(true);
         val.setConstantValue("null");
         val.setType(null);
      }
   }

@Override
public void visit(AstAssign node) {
	// TODO Auto-generated method stub
	
}

@Override
public void visit(AstConcat node) {
	// TODO Auto-generated method stub
	
}

@Override
public void visit(AstDotSuffix node) {
	// TODO Auto-generated method stub
	
}

@Override
public void visit(AstLambdaExpression node) {
	// TODO Auto-generated method stub
	
}

@Override
public void visit(AstLambdaParameters node) {
	// TODO Auto-generated method stub
	
}

@Override
public void visit(AstListData node) {
	// TODO Auto-generated method stub
	
}

@Override
public void visit(AstMapData node) {
	// TODO Auto-generated method stub
	
}

@Override
public void visit(AstMapEntry node) {
	// TODO Auto-generated method stub
	
}

@Override
public void visit(AstMethodArguments node) {
	// TODO Auto-generated method stub
	
}

}
