package com.wxxr.mobile.stock.client.util;

import java.io.IOException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class DES {
	private Key key;

	public DES() {

	}

	public DES(String shareKey) throws Exception {
		initKey(shareKey);
	}

	public void initKey(String shareKey) throws Exception {
		String strKey = shareKey;
		DESKeySpec desKeySpec = new DESKeySpec(getKey(strKey.getBytes()));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey deskey = keyFactory.generateSecret(desKeySpec);
		this.key = deskey;
	}

	private byte[] getKey(byte[] arrBTmp) throws Exception {
		if (arrBTmp.length < 8) {
			byte[] arrB = new byte[8];
			for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
				arrB[i] = arrBTmp[i];
			}
			return arrB;
		} else
			return arrBTmp;
	}

	public String getKeyValue() {
		String keyValue = this.encodeBytes(this.key.getEncoded());
		return keyValue;
	}

	public String getEncString(String message) throws Exception {
		byte[] byteMessage;
		byte[] byteMessages;
		String strMessage = "";

		try {
			byteMessages = message.getBytes();
			byteMessage = this.getEncCode(byteMessages);
			strMessage = this.encodeBytes(byteMessage);
		} finally {

			byteMessages = null;
			byteMessage = null;
		}
		return strMessage;
	}

	public String getDesString(String Message) throws Exception {

		byte[] byteMessages = null;
		byte[] byteMessage = null;
		String strMessage = "";

		byteMessage = this.decode(Message);
		byteMessages = this.getDesCode(byteMessage);
		strMessage = new String(byteMessages);

		return strMessage;
	}

	public byte[] getEncCode(byte[] byteCode) throws Exception {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byteFina = cipher.doFinal(byteCode);
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	public byte[] getDesCode(byte[] byteCode) throws Exception {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byteFina = cipher.doFinal(byteCode);
		} finally {
			cipher = null;
		}
		return byteFina;

	}

	protected byte[] decode(String message) throws IOException {
		return Base64.decode(message.getBytes(), Base64.NO_WRAP);
	}

	private String encodeBytes(byte[] encoded) {
		return Base64.encodeToString(encoded, Base64.NO_WRAP); //Base64.DEFAULT
	}
}
