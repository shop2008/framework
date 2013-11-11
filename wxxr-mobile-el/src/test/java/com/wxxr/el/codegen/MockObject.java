package com.wxxr.el.codegen;
/*
 * @(#)TestObject.java	 May 11, 2011
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */



public class MockObject {
   private MockMessage[] messages;
   
   public MockMessage[] getAllMessages(){
      return this.messages;
   }
   
   public boolean isEmpty(){
      return (this.messages == null)||(this.messages.length == 0);
   }

   
   public MockMessage lookupMessage(int idx){
      return this.messages[idx];
   }

   /**
    * Set the messages.
    * 
    * @param messages The messages to set.
    */
   public void setMessages(MockMessage[] messages) {
      this.messages = messages;
   }
}
