package com.wxxr.el.codegen;
/*
 * @(#)MessageIndexer.java	 May 11, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */



public class MessageIndexer {
   private int pointer;
   
   public int currentIndex() {
      return this.pointer;
   }

   /**
    * Set the pointer.
    * 
    * @param pointer The pointer to set.
    */
   public void setPointer(int pointer) {
      this.pointer = pointer;
   }
}
