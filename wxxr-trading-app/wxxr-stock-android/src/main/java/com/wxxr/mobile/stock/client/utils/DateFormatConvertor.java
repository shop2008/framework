package com.wxxr.mobile.stock.client.utils;

import java.util.Map;

import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValidationException;
import com.wxxr.mobile.core.util.StringUtils;

public class DateFormatConvertor implements IValueConvertor<String, String> {

	
	
	String subString1;
	
	String subString2;
	
	String nullString;
	@Override
	public Class<String> getSourceType() {
		return String.class;
	}

	@Override
	public Class<String> getTargetType() {
		return String.class;
	}

	@Override
	public String toSourceTypeValue(String input) throws ValidationException {
		return null;
	}

	@Override
	public String toTargetTypeValue(String value) {
		
		if(StringUtils.isBlank(value)){
			return this.nullString;
		}
		
		int preSubLen = 0;
		int postSubLen = 0;
		if(!StringUtils.isBlank(this.subString1)) {
			preSubLen = this.subString1.length();
			
			return value.substring(0, preSubLen);
		}
		
		if(!StringUtils.isBlank(this.subString2)) {
			postSubLen = this.subString2.length();
			return value.substring((value.length()-postSubLen), value.length());
		}
		return null;
	}

	@Override
	public void init(IWorkbenchRTContext ctx, Map<String, Object> params) {
		if(params.containsKey("subString1")){
			this.subString1 = (String) params.get("subString1");
		}
		
		if(params.containsKey("subString2")){
			this.subString2 = (String) params.get("subString2");
		}
		
		if(params.containsKey("nullString")){
			this.nullString = (String) params.get("nullString");
		}
	}

	@Override
	public void destroy() {

	}

}
