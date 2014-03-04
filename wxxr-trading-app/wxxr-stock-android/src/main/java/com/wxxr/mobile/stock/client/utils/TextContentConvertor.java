package com.wxxr.mobile.stock.client.utils;

import java.util.Map;

import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValidationException;
import com.wxxr.mobile.core.util.StringUtils;

public class TextContentConvertor implements IValueConvertor<String, String> {

	String nullString;
	String format = "\\*\\*";
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
		if(StringUtils.isBlank(value)) {
			return "";
		}
		
		String ss[]= value.split(format);
		StringBuilder sb = null;
		if(ss != null && ss.length >0) {
			
			sb = new StringBuilder();
			for(int i=0;i<ss.length;i++) {
				sb.append(ss[i]);
				if(i<ss.length)
					sb.append("\n");
			}
		}
		return sb != null ? sb.toString():"";
	}

	@Override
	public void init(IWorkbenchRTContext ctx, Map<String, Object> params) {
		if (params != null && params.containsKey("nullString")) {
			this.nullString = (String) params.get("nullString");
		}
		
		if (params != null && params.containsKey("format")) {
			this.format = (String) params.get("format");
		}
	}

	@Override
	public void destroy() {

	}

}
