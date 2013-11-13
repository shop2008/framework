package com.wxxr.mobile.stock.security.impl;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.wxxr.mobile.stock.security.IHttpRequest;
import com.wxxr.mobile.stock.security.ProtocolCommon;

public class HttpRequest implements IHttpRequest {

	private int readTimeout = 10 * 1000;
	private int connectTimeout = 10 * 1000;
	private String uri;
	private byte[] input;
	private String httpMethod;
	private Map<String, String> httpHeaders = new HashMap<String, String>();
	private String data;
	public String getData() {
		return data;
	}
	HttpRequest() {
		if (ProtocolCommon.isOpenZip){
			this.addHttpHeader("Accept-Encoding", "gzip");
		}
		this.addHttpHeader(ProtocolCommon.SECUIRTY_LEVEL,	ProtocolCommon.SECUIRTY_LEVEL_1);
		this.addHttpHeader(ProtocolCommon.CHANNEL_KEY, "true");

	}
	public HttpRequest(String uri, String method) {
		this(uri, method,null);
	}
	public HttpRequest(String uri, String method, String data) {
		this.data=data;
		if (ProtocolCommon.isOpenZip){
			this.addHttpHeader("Accept-Encoding", "gzip");
		}		this.setHttpMethod(method);
		this.setUri(uri);
		this.addHttpHeader("Accept", ProtocolCommon.JSON);
		this.addHttpHeader(ProtocolCommon.CHANNEL_KEY, "true");
		this.addHttpHeader(ProtocolCommon.CONTENT_TYPE, ProtocolCommon.JSON);
		this.addHttpHeader(ProtocolCommon.SECUIRTY_LEVEL,ProtocolCommon.SECUIRTY_LEVEL_1);

		if (data != null) {
			this.setInput(data.getBytes());
		}
	}

	public void setInput(byte[] input) {
		this.input = input;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	@Override
	public Map<String, String> getHttpHeaders() {
		return this.httpHeaders;
	}

	@Override
	public byte[] getInput() {
		return this.input;
	}

	@Override
	public String getHttpMethod() {
		return this.httpMethod;
	}

	@Override
	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public void setHttpHeaders(Map<String, String> httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

	@Override
	public int getConnectTimeout() {
		return connectTimeout;
	}

	@Override
	public int getReadTimeout() {
		return readTimeout;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("==============request====begin====================\n");
		sb.append("readTimeout =" + String.valueOf(readTimeout) + "\n");
		sb.append("connectTimeout =" + String.valueOf(connectTimeout) + "\n");
		sb.append("uri =" + String.valueOf(uri) + "\n");
		String tmp = null;
		try {
			tmp = input == null ? null : new String(input, "utf-8");
		} catch (UnsupportedEncodingException e) {
			tmp = "UnsupportedEncodingException";
		}
		sb.append("input =" + String.valueOf(tmp) + "\n");
		sb.append("httpMethod =" + String.valueOf(httpMethod) + "\n");
		sb.append("httpHeaders =" + String.valueOf(httpHeaders) + "\n");
		sb.append("==============request====end======================\n");
		return sb.toString();
	}

	@Override
	public String getHttpHeader(String key) {
		if (httpHeaders != null) {
			return httpHeaders.get(key);
		}
		return null;
	}

	public void addHttpHeader(String key, String value) {
		httpHeaders.put(key, value);

	}

	public HttpRequest clone() {
		HttpRequest x = new HttpRequest();
		x.setConnectTimeout(this.getConnectTimeout());
		Iterator i=this.getHttpHeaders().keySet().iterator();
		Map<String, String> httpHeaders = new HashMap<String, String>();
		while(i.hasNext()){
			String key=(String) i.next();
			httpHeaders.put(key, this.httpHeaders.get(key));
		}
		x.setHttpHeaders(httpHeaders);
		x.setHttpMethod(this.getHttpMethod());
		x.setInput(this.getInput());
		x.setReadTimeout(this.getReadTimeout());
		x.setUri(this.getUri());
		
		return x;
	}
}
