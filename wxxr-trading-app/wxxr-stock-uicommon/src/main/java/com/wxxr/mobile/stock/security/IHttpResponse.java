package com.wxxr.mobile.stock.security;


public interface IHttpResponse {
	
	   public int getStatus();

//	   public  Map<String, List<String>> getHeaders();

	   public  byte[] getOutput();
  	   
	   public String getBody();
	   
//	   public String getHeader(String key);
	   public boolean isSuccess();
}
