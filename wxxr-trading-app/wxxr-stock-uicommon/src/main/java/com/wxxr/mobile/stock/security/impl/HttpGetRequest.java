package com.wxxr.mobile.stock.security.impl;

import com.wxxr.mobile.stock.security.ProtocolCommon;

public class HttpGetRequest extends HttpRequest {
	public HttpGetRequest(){
		super();
	}
	public HttpGetRequest(String uri,String security_level){
		this(uri,null,null,security_level);
	}
	public HttpGetRequest(String uri){
		this(uri,null,null,null);
	}
	public HttpGetRequest(String uri,String contentType, String accept,String security_level){
		super();
		this.setHttpMethod("GET");
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
	}
}
