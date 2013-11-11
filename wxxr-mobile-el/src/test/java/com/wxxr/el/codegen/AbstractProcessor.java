package com.wxxr.el.codegen;
/*
 * @(#)AbstractProcessor.java	 May 17, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */



public class AbstractProcessor implements IProcessor {

   public AbstractProcessor() {
      super();
   }

   public final Object doProcess(IProcessingContext context) throws Exception {
      return processing(context);
   }

   protected Object processing(IProcessingContext context) throws Exception{
      return null;
   }
}
