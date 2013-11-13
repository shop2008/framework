/*
 * @(#)ProtocolCommon.java 2011-8-25
 * 
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.stock.security;

public class ProtocolCommon {
   public static final String CONTEX_PATH = "/wxxr-stock-web";
   public static final String USER_NAME_TYPE_DEVICEID = "1";
   public static final String USER_NAME_TYPE_MSISDN = "2";
   // http method
   public final static String GET = "GET";
   public final static String POST = "POST";
   public final static String CHANNEL_KEY = "Wxxr_Channel";
   public final static String DEVICEID = "deviceid";
   public final static String SPECIALEDITION = "specialEdition";
   // ����ͷ��Ϣ,ture Ϊ����
   public final static String SECUIRTY_LEVEL = "Wxxr_Secuirty_LeveL";
   public final static String SECUIRTY_LEVEL_1 = "1";//������
   public final static String SECUIRTY_LEVEL_2 = "2";//������

   public final static String SIGN_KEY = "Wxxr_Sign";
   public final static String SEQ_KEY = "Wxxr_Seq";
   public final static String AUTHORIZATION="Authorization";
   public final static String COOKIE="Cookie";
   //Digest�ָ���
   public static final String SEPARATOR = ":";
   //status code
   public final static int SEQ_CHECK_ERROR_STATUS = 900;
   public final static int SIGN_CHECK_ERROR_STATUS = 901;
//   public final static int SIGN_CHECK_ERROR_STATUS = 902;
   //char-set
   public final static String utf_8="UTF-8" ;
   public final static String CHECK_KEY="SeqAndSignCheck" ;

   //content-type
   public final static String CONTENT_TYPE="Content-type" ;
   public final static String JSON="application/json" ;
   public final static String XML="application/xml" ;

   //Content-Encoding
   public final static String CONTENT_ENCODING="Content-Encoding" ;
   public final static String GZIP="gzip";

   //Open zip
   public final static boolean isOpenZip=true;

   //login user,passwd persistent
   public final static String USERNAME_KEY="UserName" ;
   public final static String PASSWD_KEY="Passwd" ;
   public final static String SYNC_PASSWD="Sync_Passwd" ;
   //login status
   public final static String LOGIN_STATUS_KEY="LOGIN_STATUS_KEY" ;
   public final static String LOGIN_STATUS_SUCESSED="SUCESSED" ;
   public final static String LOGIN_STATUS_FAILED="FAILED" ;
   // login exception
   public final static String LOGIN_USERNAME_ERROR="username_error" ;
   public final static String LOGIN_PASSWD_ERROR="passwd_error" ;
   public final static String LOGIN_LICENCE_ERROR="licence_error" ;
   

}
