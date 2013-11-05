/*
 * @(#)ELException.java	 May 10, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.el.generator;

/**
 * @class desc A ELException.
 * 
 * @author Neil
 * @version v1.0 
 * @created time May 10, 2011  5:15:13 PM
 */
public class ELException extends RuntimeException {
   /** The serialVersionUID */
   private static final long serialVersionUID = 9002601870549713693L;


   public ELException() {
      super();
   }

   public ELException(String message, Throwable cause) {
      super(message, cause);
   }

   public ELException(String message) {
      super(message);
   }

   public ELException(Throwable cause) {
      super(cause);
   }

}
