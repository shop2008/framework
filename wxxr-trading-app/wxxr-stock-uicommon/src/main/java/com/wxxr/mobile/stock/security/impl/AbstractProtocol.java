/*
 * @(#)Protocol.java	 2011-8-25
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.stock.security.impl;

import java.io.IOException;

import com.wxxr.mobile.stock.security.IProtocol;

public abstract class AbstractProtocol implements IProtocol {
   private IProtocol next;

   @Override
   public IProtocol getNext() {
      return this.next;
   }

   public void setNext(IProtocol next) {
      this.next = next;
   }

   
   protected HttpResponse down(HttpRequest request) throws IOException {
      if (getNext()!=null){
         return getNext().service(request);
      }
      return null;
   }
}
