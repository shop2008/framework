package com.wxxr.el.codegen;
/*
 * @(#)JavassitCompiler.java	 May 17, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */



import java.util.concurrent.atomic.AtomicInteger;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import com.sun.el.parser.ELParser;
import com.sun.el.parser.SimpleNode;
import com.wxxr.mobile.core.util.StringUtils;

public class JavassitCompiler {
   
   private static AtomicInteger seqNo = new AtomicInteger(0);
   
   public static String getWrapper(ValueExpression e){
      Class<?> type = e.getType();
      if(type == Byte.TYPE){
         return "new Byte("+e.getVarName()+")";
      }else if(type == Short.TYPE){
         return "new Short("+e.getVarName()+")";
      }else if(type == Integer.TYPE){
         return "new Integer("+e.getVarName()+")";
      }else if(type == Long.TYPE){
         return "new Long("+e.getVarName()+")";
      }else if(type == Character.TYPE){
         return "new Character("+e.getVarName()+")";
      }else if(type == Float.TYPE){
         return "new Float("+e.getVarName()+")";
      }else if(type == Double.TYPE){
         return "new Double("+e.getVarName()+")";
      }else if(type == Boolean.TYPE){
         return "new Boolean("+e.getVarName()+")";
      }
      return null;
   }
   
   private ClassPool classPool;

   public IProcessor compile(String expression) throws Exception {
      if(StringUtils.isBlank(expression)){
          throw new IllegalArgumentException("Invalid java expression :"+expression);
      }
      String expr = StringUtils.trim(expression);
      ClassPool pool = getClassPool();
      String baseClass =  AbstractProcessor.class.getCanonicalName();
      CtClass ctClass = pool.get(baseClass);
      CtClass ctxClass = pool.get(IProcessingContext.class.getCanonicalName());
      String className = new StringBuffer("com.wxxr.el.parser.Processor").append(System.currentTimeMillis()).append(seqNo.getAndIncrement()).toString();
      ctClass.setName(className);
      CtMethod m = ctClass.getDeclaredMethod("processing", new CtClass[]{ctxClass});
      SimpleNode node = (SimpleNode) ELParser.parse(expr);
      CompilingContext compilingContext = new CompilingContext();
      ValueExpression val = new ValueExpression();
      compilingContext.setValue(val);
      compilingContext.setResolver(new MockDomainResolver());
      val.setVarName(compilingContext.getNextVarName());
      node.accept(new JavaDialect(compilingContext));
      System.out.println("\n=============================\n");
      StringBuffer buf = new StringBuffer("{\n");
      buf.append(IProcessingContext.class.getCanonicalName()).append(" context = $1;\n");
      val.generateJavaCode(compilingContext,buf);
      if(val.getType() == Void.class){
         buf.append("\nreturn null;\n");
      }else if(val.getType().isPrimitive()){
         buf.append("\nreturn ").append(getWrapper(val)).append(";\n");
      }else{
         buf.append("\nreturn ").append(val.getVarName()).append(";\n");
      }
      buf.append("\n\t}");
      System.out.print("====================METHOD��BODY====================\n");
      System.out.println(buf.toString());
      m.setBody(buf.toString());
      Class<?> clazz = ctClass.toClass();
      System.out.print("===================CtClass====================\n");
      System.out.println(clazz);
      return (IProcessor)clazz.newInstance();
  }

   protected ClassPool getClassPool() {
      if(classPool == null){
          classPool = new ClassPool();
          classPool.appendSystemPath();
          classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
      }
      return classPool;
  }

}
