package com.wxxr.el.codegen;
/*
 * @(#)TestProcessingContext.java	 May 17, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */



import java.util.HashMap;

/**
 * @class desc A TestProcessingContext.
 * 
 * @author Neil
 * @version v1.0 
 * @created time May 17, 2011  3:47:25 PM
 */
public class MockProcessingContext implements IProcessingContext {
   private HashMap<String, Object> beans = new HashMap<String, Object>();

   public Object getBean(String name) {
      return beans.get(name);
   }
   
   public void setBean(String name, Object bean){
      this.beans.put(name, bean);
   }

}
