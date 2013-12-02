package com.wxxr.mobile.stock.client.utils;

import java.util.Map;

import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValidationException;

public class String2StringConvertor implements IValueConvertor<String, String> {

	private String replace = "x";
	
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
		if (value == null) {
			return null;
		}
		
		int length = value.length();
		if (length > 0) {
			String firstStr = value.substring(0,1);
			value = value.replace(firstStr, this.replace);
			return value;
		}
		return null;
	}

	@Override
	public void init(IWorkbenchRTContext ctx, Map<String, Object> params) {
		if (params.containsKey("replace")) {
			this.replace = (String) params.get("replace");
		}
	}

	@Override
	public void destroy() {
		
	}

}
