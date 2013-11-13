/*
 * @(#)A.java	 2011-8-11
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.stock.client.util;

import java.util.UUID;

public class StringUtils {
   private static final int PAD_LIMIT = 8192;
   private static final String[] PADDING = new String[Character.MAX_VALUE];

   public static String leftPad(String str, int size) {
      return leftPad(str, size, ' ');
  }
   public static String leftPad(String str, int size, char padChar) {
      if (str == null) {
          return null;
      }
      int pads = size - str.length();
      if (pads <= 0) {
          return str; // returns original String when possible
      }
      if (pads > PAD_LIMIT) {
          return leftPad(str, size, String.valueOf(padChar));
      }
      return padding(pads, padChar).concat(str);
  }
   public static String leftPad(String str, int size, String padStr) {
      if (str == null) {
          return null;
      }
      if (isEmpty(padStr)) {
          padStr = " ";
      }
      int padLen = padStr.length();
      int strLen = str.length();
      int pads = size - strLen;
      if (pads <= 0) {
          return str; // returns original String when possible
      }
      if (padLen == 1 && pads <= PAD_LIMIT) {
          return leftPad(str, size, padStr.charAt(0));
      }

      if (pads == padLen) {
          return padStr.concat(str);
      } else if (pads < padLen) {
          return padStr.substring(0, pads).concat(str);
      } else {
          char[] padding = new char[pads];
          char[] padChars = padStr.toCharArray();
          for (int i = 0; i < pads; i++) {
              padding[i] = padChars[i % padLen];
          }
          return new String(padding).concat(str);
      }
  }
   private static String padding(int repeat, char padChar) {
      // be careful of synchronization in this method
      // we are assuming that get and set from an array index is atomic
      String pad = PADDING[padChar];
      if (pad == null) {
          pad = String.valueOf(padChar);
      }
      while (pad.length() < repeat) {
          pad = pad.concat(pad);
      }
      PADDING[padChar] = pad;
      return pad.substring(0, repeat);
  }
   public static boolean isEmpty(String str) {
      return (str == null || str.length() == 0);
  }
   public static boolean isNotBlank(String str) {
      int strLen;
      if (str == null || (strLen = str.length()) == 0) {
          return false;
      }
      for (int i = 0; i < strLen; i++) {
          if ((Character.isWhitespace(str.charAt(i)) == false)) {
              return true;
          }
      }
      return false;
  }
   public static boolean isBlank(String str) {
     return !isNotBlank(str);
  }
   public static String rightPad(String str, int size) {
      return rightPad(str, size, ' ');
  }
   public static String rightPad(String str, int size, char padChar) {
      if (str == null) {
          return null;
      }
      int pads = size - str.length();
      if (pads <= 0) {
          return str; // returns original String when possible
      }
      if (pads > PAD_LIMIT) {
          return rightPad(str, size, String.valueOf(padChar));
      }
      return str.concat(padding(pads, padChar));
  }
   public static String rightPad(String str, int size, String padStr) {
      if (str == null) {
          return null;
      }
      if (isEmpty(padStr)) {
          padStr = " ";
      }
      int padLen = padStr.length();
      int strLen = str.length();
      int pads = size - strLen;
      if (pads <= 0) {
          return str; // returns original String when possible
      }
      if (padLen == 1 && pads <= PAD_LIMIT) {
          return rightPad(str, size, padStr.charAt(0));
      }

      if (pads == padLen) {
          return str.concat(padStr);
      } else if (pads < padLen) {
          return str.concat(padStr.substring(0, pads));
      } else {
          char[] padding = new char[pads];
          char[] padChars = padStr.toCharArray();
          for (int i = 0; i < pads; i++) {
              padding[i] = padChars[i % padLen];
          }
          return str.concat(new String(padding));
      }
  }
   public static String generateUuId() {
      UUID uuid = UUID.randomUUID();
      String id = uuid.toString().replaceAll("-", "");
      return id;
   }
}
