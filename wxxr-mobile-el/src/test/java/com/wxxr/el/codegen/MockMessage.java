package com.wxxr.el.codegen;
/*
 * @(#)TestMessage.java	 May 16, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */



public class MockMessage {
   private Long id;
   private String text;
   /**
    * Get the id.
    * 
    * @return the id.
    */
   public Long getId() {
      return id;
   }
   /**
    * Get the text.
    * 
    * @return the text.
    */
   public String getText() {
      return text;
   }
   /**
    * Set the id.
    * 
    * @param id The id to set.
    */
   public void setId(Long id) {
      this.id = id;
   }
   /**
    * Set the text.
    * 
    * @param text The text to set.
    */
   public void setText(String text) {
      this.text = text;
   }
}
