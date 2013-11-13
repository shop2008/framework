/*
 * @(#)EnDeCodeProtocol.java 2011-8-25
 * 
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.stock.security.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.wxxr.mobile.stock.client.util.DES;
import com.wxxr.mobile.stock.client.util.RSACoder;
import com.wxxr.mobile.stock.client.util.StringUtils;
import com.wxxr.mobile.stock.security.IConnectorContext;
import com.wxxr.mobile.stock.security.ProtocolCommon;

/**
 * 
 * @class desc A EnDeCodeProtocol.
 * 
 * @author zhengjincheng
 * @version v1.0
 */
public class EnDeCodeProtocol extends AbstractProtocol {
	Logger log = Logger.getLogger(EnDeCodeProtocol.class);

	private byte[] publicKey;// ����˹�Կ
	private byte[] privateKey;// �ͻ���˽Կ
	private IConnectorContext context;
	private AbstractState currState;
	private DES des = new DES();
	private int cseq = 1;// ��¼Ϊ1
	private int sseq = 0;
	private String jsessionid;
	private String share_key;
	
	private String getCseq() {
		cseq++;
		return String.valueOf(cseq);
	}

	private void initializeDES(String sharekey) {
		try {
			des.initKey(sharekey);
		} catch (Exception e) {
			log.error("init DES share key error! " + e.getMessage());
		}

	}

	public EnDeCodeProtocol(IConnectorContext context) {
		this.context=context;
		this.publicKey = getPublicKey();
		this.privateKey = getPrivateKey();		
	}

	public HttpResponse doservice(HttpRequest request) throws IOException {
		if (ProtocolCommon.SECUIRTY_LEVEL_2.equals(request
				.getHttpHeader(ProtocolCommon.SECUIRTY_LEVEL))) {
			if (request.getInput() != null) {
				byte[] data = request.getInput();
				try {
					byte[] en_data = des.getEncCode(data);
					request.setInput(en_data);
				} catch (Exception e) {
					log.error("getEncCode error! " + e.getMessage());
					return null;
				}
			}
		}
		String cseq_t = getCseq();
		request.addHttpHeader(ProtocolCommon.SEQ_KEY, cseq_t);

		if (request.getHttpMethod().equals(ProtocolCommon.GET)) {
			String sign = getClientSign((ProtocolCommon.GET + ":" + cseq_t)
					.getBytes());
			request.addHttpHeader(ProtocolCommon.SIGN_KEY, sign);
		}
		if (request.getHttpMethod().equals(ProtocolCommon.POST)) {
			byte[] data = null;

			data = addBytes(
					(ProtocolCommon.POST + ":" + cseq_t + ":").getBytes(),
					request.getInput());

			String sign = getClientSign(data);
			request.addHttpHeader(ProtocolCommon.SIGN_KEY, sign);
		}
		HttpResponse res = this.getNext().service(request);
		if (res.getHeader(ProtocolCommon.CHANNEL_KEY)!=null){
		try {
			String sseq = res.getHeader(ProtocolCommon.SEQ_KEY);
			if (!checksseq(sseq)) {
				// res.setCode(ProtocolCommon.SEQ_CHECK_ERROR_STATUS);
			}
			String sign = res.getHeader(ProtocolCommon.SIGN_KEY);
			if (sign == null) {
				log.error(res.toString());
				throw new Exception("sign is null error!");
			}
			byte[] sign_data;
			if (res.getOutput() == null||res.getOutput().length<1) {
				sign_data = sseq.getBytes();
			} else {
				sign_data = res.getOutput();
			}

			Boolean result = RSACoder.verify(sign_data, publicKey,
					RSACoder.hexStrToBytes(sign));
			if (!result) {
				throw new Exception("check server sign error!");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		if (ProtocolCommon.SECUIRTY_LEVEL_2.equals(res.getHeader(ProtocolCommon.SECUIRTY_LEVEL))) {
			byte[] body = res.getOutput();
			try {
				byte[] de_body = des.getDesCode(body);
				res.setOutput(de_body);
//				log.info(" de_body =" + de_body);
			} catch (Exception e) {
				res =null;
				log.error("getDesCode error! " + e.getMessage());
			}
		}
		}
		return res;
	}

	/**
	 * 1.�ж��Ƿ��Ѿ�������֣����û��������֣���������� 2.������ֺ��ٸ������İ�ȫ���� SECUIRTY_LEVEL
	 * �����SEQ��ǩ��Ρ�����
	 */
	@Override
	public  HttpResponse service(HttpRequest request) throws IOException {
		if (currState == null || currState instanceof  NotHandShakeState){
			synchronized (this) {
				HttpResponse r=realDoService(request);
				return r;
			}
		}else{
			HttpResponse r=realDoService(request);
			return r;
		}
	}

	
	public  HttpResponse realDoService(HttpRequest request) throws IOException {
		
		if (currState == null) {
			NotHandShakeState s = new NotHandShakeState();
			s.setRequest(request);
			setState(s);
		}
		syncPwd(request);
		currState.setRequest(request);
		HttpResponse res = currState.service();
		if ((res != null && (res.getStatus() == 901 || res.getStatus() == 400)) || is901Error(res)){
			NotHandShakeState s = new NotHandShakeState();
			s.setRequest(request);
			this.setState(s);
			return currState.service();
		}
		return res;
	}
	private boolean is901Error(HttpResponse res) {
		if (res != null) {
			if (res.getStatus() == 500) {
				if (res.getError_output() != null) {
					String x;
					try {
						x = new String(res.getError_output(), "utf-8");
						if (x.indexOf("HTTP Status 901") > 0) {
							return true;
						}
					} catch (UnsupportedEncodingException e) {
						return false;
					}

				}
			}
		}
		return false;
	}

	public void doHandShake() throws IOException {
		HttpRequest request = new HttpRequest();
		request.setHttpMethod(ProtocolCommon.GET);
		request.setUri("/handshake");
		
		HttpResponse res = doservice(request);
		this.jsessionid = res.getJSESSIONID();
		try {
			this.share_key=RSACoder.getDecryptbyPrivateKey(res.getOutput(), privateKey);
		} catch (Exception e) {
			log.error("get share_key error! "+e.getMessage());
		}
		initializeDES(this.share_key);
	}

	public void setState(AbstractState state) {
		if (state != currState) {
			currState = state;
		}
	}

	class NotHandShakeState extends AbstractState {

		@Override
		public HttpResponse service() {
			try {
				doHandShake();
				HandShakedState s = new HandShakedState();
				s.setRequest(request);
				setState(s);
				return currState.service();
			} catch (IOException e) {
				context.getExceptionHandler().handle(e);
				log.error("curr request uri= " + request.getUri() + ",Method= "
						+ request.getHttpMethod());
				log.error("error in NotHandShakeState![" + e.getClass().getName()+"]"+e.getMessage());
			}
			return null;
		}
	}

	class HandShakedState extends AbstractState {
		@Override
		public HttpResponse service() {
			try {
				request.addHttpHeader(ProtocolCommon.COOKIE, jsessionid);
				HttpResponse res = doservice(request);
				return res;
			} catch (IOException e) {
				context.getExceptionHandler().handle(e);
				log.error("curr request uri= " + request.getUri() + ",Method= "
						+ request.getHttpMethod());
				log.error("error in HandShakedState![" + e.getClass().getName()+"]"+e.getMessage());
			}
			return null;
		}
	}

	private boolean checksseq(String sseq) {
		if (sseq != null) {
			int s = Integer.valueOf(sseq);
			if (s > this.sseq) {
				s = this.sseq;
				return true;
			}
		}
		return false;
	}

	protected String getClientSign(byte[] data) {
		byte[] sign;
		try {
			sign = RSACoder.sign(data, privateKey);
			return RSACoder.bytesToHexStr(sign);
		} catch (Exception e) {
			log.error("sign error! " + e.getMessage());
		}
		return null;
	}

	public static byte[] addBytes(byte[] src1, byte[] src2) {
		byte[] dest = new byte[src1.length + src2.length];
		System.arraycopy(src1, 0, dest, 0, src1.length);
		System.arraycopy(src2, 0, dest, src1.length, src2.length);
		return dest;
	}

	// ��ȡ����˹�Կ
	private byte[] getPublicKey() {
		byte[] key = null;
		try {
			key = RSACoder.getPublicKey(this.context.getServerCert());
		} catch (Exception e) {
			log.info("get PublicKey error");
		}
		return key;
	}

	// ��ȡ�ͻ���˽Կ
	private byte[] getPrivateKey() {
		byte[] key = null;
		try {
			key = RSACoder.getPrivateKey(this.context.getCientCert(), "client",
					"1111111");
		} catch (Exception e) {
			log.info("get PrivateKey error");
		}
		return key;
	}
	
	//����DES����
	protected void syncPwd(HttpRequest request){
		String pwd = request.getHttpHeader(ProtocolCommon.SYNC_PASSWD);
		if(StringUtils.isNotBlank(pwd)){
			try {
				//DES����
				pwd = des.getEncString(pwd); //"rD6Znq0n9uE=";//"122345";//
			} catch (Exception e) {
				log.error("DES�����쳣��"+e.getMessage());
			}
			request.addHttpHeader(ProtocolCommon.SYNC_PASSWD, pwd);
		}
		
	}
}

