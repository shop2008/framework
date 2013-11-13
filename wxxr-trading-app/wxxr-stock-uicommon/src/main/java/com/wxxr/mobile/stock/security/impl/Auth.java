/*
 * @(#)Auth.java	 2011-8-9
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.stock.security.impl;

public class Auth {
   private String realm;
   private String nonce ;
   private String qop ;
   private String opaque ;
   public Auth(){
      
   }
   public String getRealm() {
      return realm;
   }
   public void setRealm(String realm) {
      this.realm = realm;
   }
   public String getNonce() {
      return nonce;
   }
   public void setNonce(String nonce) {
      this.nonce = nonce;
   }
   public String getQop() {
      return qop;
   }
   public void setQop(String qop) {
      this.qop = qop;
   }
   public String getOpaque() {
      return opaque;
   }
   public void setOpaque(String opaque) {
      this.opaque = opaque;
   }
}
