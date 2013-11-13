/*
 * @(#)RSACoder.java 2011-7-13
 * 
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.stock.client.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSACoder {

   public static final String KEY_ALGORITHM = "RSA";
   public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
   private static final String PUBLIC_KEY = "RSAPublicKey";
   private static final String PRIVATE_KEY = "RSAPrivateKey";
   public static final String X509 = "X.509"; 
   public static final String KEY_STORE = "JKS";
   private static final int KEY_SIZE = 512;
	/**
	 * 签名
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
   public static byte[] sign(byte[] data, byte[] privateKey)
         throws NoSuchAlgorithmException, InvalidKeySpecException,
         InvalidKeyException, SignatureException {
      PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
      KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
      PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
      Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
      signature.initSign(priKey);
      signature.update(data);
      return signature.sign();
   }

   /**
    * 验证签名
    */
   public static boolean verify(byte[] data, byte[] publicKey, byte[] sign)
         throws Exception {

      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
      KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
      PublicKey pubKey = keyFactory.generatePublic(keySpec);
      Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
      signature.initVerify(pubKey);
      signature.update(data);
      return signature.verify(sign);
   }

   /**
    * 从密钥对中获取私钥
    * @param keyMap
    * @return
    * @throws Exception
    */
   public static byte[] getPrivateKey(Map<String, Object> keyMap) throws Exception {

      Key key = (Key) keyMap.get(PRIVATE_KEY);

      return key.getEncoded();
   }

	/**
	 * 从密钥对中获取公钥
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
   public static byte[] getPublicKey(Map<String, Object> keyMap) throws Exception {

      Key key = (Key) keyMap.get(PUBLIC_KEY);

      return key.getEncoded();
   }

   /**
    * 生成密钥对
    * 
    * @return Map ��Կ�Զ� Map
    * @throws Exception
    */
   public Map<String, Object> initKey() throws Exception {

      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);

      keyPairGen.initialize(KEY_SIZE);

      KeyPair keyPair = keyPairGen.generateKeyPair();

      RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

      RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

      Map<String, Object> keyMap = new HashMap<String, Object>(2);

      keyMap.put(PUBLIC_KEY, publicKey);
      keyMap.put(PRIVATE_KEY, privateKey);

      return keyMap;
   }
   /**
    * 公钥加密
    * 
    * 
    * @param originalString
    * @param publicKeyArray
    * @return
    * @throws NoSuchAlgorithmException 
    * @throws InvalidKeySpecException 
    * @throws NoSuchPaddingException 
    * @throws InvalidKeyException 
    * @throws BadPaddingException 
    * @throws IllegalBlockSizeException 
    * @throws Exception
    * ����ʱ�䣺2010-12-1 ����06:29:51
    */
   public static byte[] getEncryptTobyte(String originalString,byte[] publicKey) throws Exception {
       X509EncodedKeySpec keySpec=new X509EncodedKeySpec(publicKey);
       KeyFactory kf=KeyFactory.getInstance(KEY_ALGORITHM);
       PublicKey keyPublic=kf.generatePublic(keySpec);
       
       Cipher cp=Cipher.getInstance("RSA/ECB/PKCS1Padding");
       cp.init(Cipher.ENCRYPT_MODE, keyPublic);
       return cp.doFinal(originalString.getBytes());
   }
   
   public static String getEncryptToHexStr(String originalString,byte[] publicKey) throws Exception{
      byte[] result=getEncryptTobyte(originalString,publicKey);
      return bytesToHexStr(result);
  }
	/**
	 * 私钥解密
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */

   public static String getDecryptbyPrivateKey(byte[] data, byte[] privateKey) throws Exception{
       PKCS8EncodedKeySpec keySpec=new PKCS8EncodedKeySpec(privateKey);
       KeyFactory kf=KeyFactory.getInstance(KEY_ALGORITHM);
       PrivateKey keyPrivate=kf.generatePrivate(keySpec);       
       Cipher cp=Cipher.getInstance("RSA/ECB/PKCS1Padding");
       cp.init(Cipher.DECRYPT_MODE, keyPrivate);
       byte[] arr=cp.doFinal(data);       
       return new String(arr);
   }
   public static String getDecryptbyPrivateKey(String hexStrdata, byte[] privateKey) throws Exception{
      byte[] data= hexStrToBytes(hexStrdata);
      String result=getDecryptbyPrivateKey(data,privateKey);
      return result;
  }


   /**
    * 字符串转十六进制
    */
   public static final String bytesToHexStr(byte[] bcd) {
      StringBuffer s = new StringBuffer(bcd.length * 2);
      for (int i = 0; i < bcd.length; i++) {
         s.append(bcdLookup[(bcd[i] >>> 4) & 0x0f]);
         s.append(bcdLookup[bcd[i] & 0x0f]);
      }
      return s.toString();
   }

   /**
    * 十六进制转字符串
    */
   public static final byte[] hexStrToBytes(String s) {
      byte[] bytes;

      bytes = new byte[s.length() / 2];

      for (int i = 0; i < bytes.length; i++) {
         bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
      }
      return bytes;
   }

   private static final char[] bcdLookup = {'0', '1', '2', '3', '4', '5', '6',
         '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
   
 //客户端私钥 for client
   public static  byte[] getPrivateKey(InputStream fis,String alias,String password) throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, UnrecoverableKeyException 
   {
         KeyStore ks = KeyStore.getInstance("PKCS12");
         char[] nPassword = StringUtils.isBlank(password)? null:password.toCharArray();
         ks.load(fis, nPassword);
         fis.close();
         PrivateKey privatekey = (PrivateKey) ks.getKey(alias, nPassword);
         return privatekey.getEncoded();
   }
   //服务端公钥 for client
   public static byte[] getPublicKey(InputStream fis) throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, UnrecoverableKeyException 
   {
	   CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);
       Certificate certificate = certificateFactory.generateCertificate(fis);
       fis.close();
       PublicKey publickey=certificate.getPublicKey();
       return publickey.getEncoded();
   }
   //服务端公钥、私钥 for server
   public static Map<String ,Object> getKeys(String keystore,String alias,String password) throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, UnrecoverableKeyException 
   {
         KeyStore ks = KeyStore.getInstance(KEY_STORE);
         char[] nPassword = StringUtils.isBlank(password)? null:password.toCharArray();
         FileInputStream fis = new FileInputStream(keystore);
         ks.load(fis, nPassword);
         fis.close();
         PrivateKey privateKey = (PrivateKey) ks.getKey(alias, nPassword);
         Certificate cert = ks.getCertificate(alias);
         PublicKey publicKey = cert.getPublicKey();
         Map<String, Object> keyMap = new HashMap<String, Object>(2);
         keyMap.put(PUBLIC_KEY, publicKey);
         keyMap.put(PRIVATE_KEY, privateKey);
         return keyMap;
   }

}
