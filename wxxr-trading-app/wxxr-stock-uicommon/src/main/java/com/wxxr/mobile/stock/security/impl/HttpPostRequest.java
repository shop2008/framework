package com.wxxr.mobile.stock.security.impl;

import com.wxxr.mobile.stock.security.ProtocolCommon;

public class HttpPostRequest extends HttpRequest {
	
	public HttpPostRequest(String uri,String data ){
		this(uri,null,null,data,null);
	}
	public HttpPostRequest(String uri,String data,String security_level ){
		this(uri,null,null,data,security_level);
	}
	public HttpPostRequest(String uri,String contentType, String accept,String data,String security_level){
		super();
		this.setHttpMethod("POST");
		this.setUri(uri);
		if (accept==null){
			this.addHttpHeader("Accept", ProtocolCommon.JSON);
		}else{
			this.addHttpHeader("Accept",accept);
		}
		if (contentType==null){
			this.addHttpHeader(ProtocolCommon.CONTENT_TYPE, ProtocolCommon.JSON);
		}else{
			this.addHttpHeader(ProtocolCommon.CONTENT_TYPE,contentType);
		}
		if (security_level==null){
			this.addHttpHeader(ProtocolCommon.SECUIRTY_LEVEL, ProtocolCommon.SECUIRTY_LEVEL_1);
		}else{
			this.addHttpHeader(ProtocolCommon.SECUIRTY_LEVEL,security_level);
		}
		if (data!=null){
			this.setInput(data.getBytes());
		}
	}
}
