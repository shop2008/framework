/*
 * @(#)AbstractState.java	 2011-9-21
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.stock.security.impl;


public abstract class AbstractState {
   protected HttpRequest request;
  
   public void setRequest(HttpRequest request){
      this.request=request;
   }
   public abstract HttpResponse service();
   
}

