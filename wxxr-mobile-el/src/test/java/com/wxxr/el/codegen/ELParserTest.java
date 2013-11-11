package com.wxxr.el.codegen;
/*
 * @(#)ELParserTest.java	 May 10, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.el.parser.ELParser;
import com.sun.el.parser.SimpleNode;

/**
 * @class desc A ELParserTest.
 * 
 * @author Neil
 * @version v1.0 
 * @created time May 10, 2011  5:34:44 PM
 */
public class ELParserTest {

   /** FIXME Comment this
    * method desc
    * @throws java.lang.Exception
    */
   @Before
   public void setUp() throws Exception {
   }

   /** FIXME Comment this
    * method desc
    * @throws java.lang.Exception
    */
   @After
   public void tearDown() throws Exception {
   }

   /**
    * Test method for {@link com.wxxr.el.parser.ELParser#parse(java.lang.String)}.
    */
   @Test
   public void testParse() {
//      parse("#{foo.a.b}");
//      parse("#{foo.a['b']}");
//      parse("#{3 * foo.a.b(e,c,d)*1000}");
//      parse("#{'foo'.length().food}");
//      parse("#{foo}");
////    parse("#{company.employees@name}");
////    parse("#{company.employees@getName()}");
////    parse("#{company.employees@each{x|x.salary}.salary}");
////    parse("#{company.employees@each{x|x.hashCode()}}");
////    parse("#{company.employees@each{x|x@max{y|y.salary}}} is complex");
//              parse("#{company.get(foo)}");
//      parse("#{company.employees.stream().forEach(x->x.getName()).toList()}");
////      parse("#{company.employees.{x|x.salary}.salary}");
////      parse("#{company.employees.{x|x.hashCode()}}");
////      parse("#{company.employees.{x|x.{y|y.salary}}} is complex");
////     
////      System.out.println("\n=============================\n");
////      parse("#{company.departments.{x|x.employees}.{x|x.lastName}}");
////      parse("#{company.departments.{x|x.employees.{x|x.lastName}}}");
//      parse("#{user.matchRole(1)}");
//      SimpleNode node = parse("#{repo.allMessages.toString()}");
//      CompilingContext val = new CompilingContext().setResolver(new MockDomainResolver());
//      node.accept(new JavaDialect(val));
//      System.out.println("\n=============================\n");
//      StringBuffer buf = new StringBuffer();
//      val.getValue().generateJavaCode(val,buf);
//      System.out.println(buf.toString());
//      System.out.println();
//      
//      node = parse("#{4*5*100}");
//      val = new CompilingContext().setResolver(new MockDomainResolver());
//      node.accept(new JavaDialect(val));
//      System.out.println("\n=============================\n");
//      buf = new StringBuffer();
//      val.getValue().generateJavaCode(val,buf);
//      System.out.println(buf.toString());
//      System.out.println();
//
//      node = parse("#{4*5/100}");
//      val = new CompilingContext().setResolver(new MockDomainResolver());
//      node.accept(new JavaDialect(val));
//      System.out.println("\n=============================\n");
//      buf = new StringBuffer();
//      val.getValue().generateJavaCode(val,buf);
//      System.out.println(buf.toString());
//      System.out.println();
//
//      node = parse("#{4.0/5*100}");
//      val = new CompilingContext().setResolver(new MockDomainResolver());
//      node.accept(new JavaDialect(val));
//      System.out.println("\n=============================\n");
//      buf = new StringBuffer();
//      val.getValue().generateJavaCode(val,buf);
//      System.out.println(buf.toString());
//      System.out.println();
//      
//      node = parse("#{repo.allMessages.length}");
//      val = new CompilingContext().setResolver(new MockDomainResolver());
//      node.accept(new JavaDialect(val));
//      System.out.println("\n=============================\n");
//      buf = new StringBuffer();
//      val.getValue().generateJavaCode(val,buf);
//      System.out.println(buf.toString());
//      System.out.println();
//
//      node = parse("#{repo.lookupMessage(indexer.currentIndex())}");
//      val = new CompilingContext().setResolver(new MockDomainResolver());
//      node.accept(new JavaDialect(val));
//      System.out.println("\n=============================\n");
//      buf = new StringBuffer();
//      val.getValue().generateJavaCode(val,buf);
//      System.out.println(buf.toString());
//      System.out.println();
//
//      node = parse("#{repo.isEmpty()||(indexer.currentIndex() < 0)}");
//      val = new CompilingContext().setResolver(new MockDomainResolver());
//      node.accept(new JavaDialect(val));
//      System.out.println("\n=============================\n");
//      buf = new StringBuffer();
//      val.getValue().generateJavaCode(val,buf);
//      System.out.println(buf.toString());
//      System.out.println();
//
//      node = parse("#{!(repo.isEmpty()||(indexer.currentIndex() < 0))}");
//      val = new CompilingContext().setResolver(new MockDomainResolver());
//      node.accept(new JavaDialect(val));
//      System.out.println("\n=============================\n");
//      buf = new StringBuffer();
//      val.getValue().generateJavaCode(val,buf);
//      System.out.println(buf.toString());
//      System.out.println();
//
//      node = parse("#{repo.isEmpty()||(indexer.currentIndex() < 0) ? 'No Message Avaiable' : repo.lookupMessage(indexer.currentIndex()).text}");
//      val = new CompilingContext().setResolver(new MockDomainResolver());
//      node.accept(new JavaDialect(val));
//      System.out.println("\n=============================\n");
//      buf = new StringBuffer("public static ");
//      buf.append(val.getValue().getType().getCanonicalName()).append(" evaluate( ISMProcessingContext context ) {\n");
//      val.getValue().generateJavaCode(val,buf);
//      buf.append("return ").append(val.getValue().getVarName()).append(";\n");
//      buf.append("}");
//      System.out.println(buf.toString());
//      
//      System.out.println();
//   
//      node = parse("#{repo.allMessages.{x|x.text}}");
//      val = new CompilingContext().setResolver(new MockDomainResolver());
//      node.accept(new JavaDialect(val));
//      System.out.println("\n=============================\n");
//      buf = new StringBuffer();
//      val.getValue().generateJavaCode(val,buf);
//      System.out.println(buf.toString());
//      System.out.println();
//
//      node = parse("#{repo.allMessages.{x|x.text}.{y|y.length()}}");
//      val = new CompilingContext().setResolver(new MockDomainResolver());
//      node.accept(new JavaDialect(val));
//      System.out.println("\n=============================\n");
//      buf = new StringBuffer();
//      val.getValue().generateJavaCode(val,buf);
//      System.out.println(buf.toString());
//      System.out.println();
   }

   @Test
   public void testCompiling() throws Exception {
//      JavassitCompiler compiler = new JavassitCompiler();
//      MockProcessingContext ctx = new MockProcessingContext();
//      IProcessor processor = compiler.compile("#{4*5*100}");
//      Object val = processor.doProcess(ctx);
//      assertTrue(val instanceof Integer);
//      assertEquals(2000, ((Integer)val).intValue());
//      
//      processor = compiler.compile("#{repo.isEmpty()||(indexer.currentIndex() < 0)}");
//      ctx = new MockProcessingContext();
//      ctx.setBean("repo", new MockObject());
//      val = processor.doProcess(ctx);
//      assertTrue(val instanceof Boolean);
//      assertTrue(((Boolean)val).booleanValue());
//
//      processor = compiler.compile("#{!(repo.isEmpty()||(indexer.currentIndex() < 0))}");
//      ctx = new MockProcessingContext();
//      ctx.setBean("repo", new MockObject());
//      val = processor.doProcess(ctx);
//      assertTrue(val instanceof Boolean);
//      assertFalse(((Boolean)val).booleanValue());
//      
//      try {
//         processor = compiler.compile("#{repo.isEmpty()||(indexer.currentIndex() < 0)}");
//         ctx = new MockProcessingContext();
//         MockObject repo = new MockObject();
//         repo.setMessages(new MockMessage[]{ new MockMessage()});
//         ctx.setBean("repo",repo );
//         val = processor.doProcess(ctx);
//         fail("Should fail here !!!");
//      }catch(NullPointerException e){        
//      }
//      
//      processor = compiler.compile("#{repo.isEmpty()||(indexer.currentIndex() < 0)}");
//      ctx = new MockProcessingContext();
//      MockObject repo = new MockObject();
//      repo.setMessages(new MockMessage[]{ new MockMessage()});
//      ctx.setBean("repo",repo );
//      ctx.setBean("indexer", new MessageIndexer());
//      val = processor.doProcess(ctx);
//      assertTrue(val instanceof Boolean);
//      assertFalse(((Boolean)val).booleanValue());
//
//      processor = compiler.compile("#{repo.isEmpty()&&(indexer.currentIndex() < 0)}");
//      ctx = new MockProcessingContext();
//      repo = new MockObject();
//      repo.setMessages(new MockMessage[]{ new MockMessage()});
//      ctx.setBean("repo",repo );
//      val = processor.doProcess(ctx);
//      assertTrue(val instanceof Boolean);
//      assertFalse(((Boolean)val).booleanValue());
//      
//      try {
//         processor = compiler.compile("#{repo.isEmpty()&&(indexer.currentIndex() < 0)}");
//         ctx = new MockProcessingContext();
//         ctx.setBean("repo",new MockObject() );
//         val = processor.doProcess(ctx);
//         fail("Should fail here !!!");
//      }catch(NullPointerException e){        
//      }
//      processor = compiler.compile("#{repo.isEmpty()&&(indexer.currentIndex() < 0)}");
//      ctx = new MockProcessingContext();
//      ctx.setBean("repo",new MockObject() );
//      MessageIndexer idx = new MessageIndexer();
//      idx.setPointer(-1);
//      ctx.setBean("indexer",idx );
//      val = processor.doProcess(ctx);
//      assertTrue(val instanceof Boolean);
//      assertTrue(((Boolean)val).booleanValue());
  }
   
  public static SimpleNode parse(String in) {
      System.out.println(in);
      SimpleNode node = (SimpleNode) ELParser.parse(in);
      node.dump("");
      System.out.println();
      return node;
  }

}
