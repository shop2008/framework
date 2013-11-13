package com.wxxr.mobile.stock.security;

import java.util.Map;

public interface IHttpRequest {
	
	Map<String, String> getHttpHeaders();
	
	String getHttpHeader(String key);
	
	byte[] getInput();
	
	String getHttpMethod();

	String getUri();

	public void setUri(String uri);

	public int getConnectTimeout();

	public int getReadTimeout();

}
