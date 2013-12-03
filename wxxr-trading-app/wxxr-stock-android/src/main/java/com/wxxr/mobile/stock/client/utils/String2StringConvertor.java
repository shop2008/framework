package com.wxxr.mobile.stock.client.utils;

import java.util.Map;

import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValidationException;
import com.wxxr.mobile.core.util.StringUtils;

public class String2StringConvertor implements IValueConvertor<String, String> {

	private String replace;

	private String format = "%.0f";
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
		if (StringUtils.isBlank(value)) {
			return null;
		}
		
		int length = value.length();
		if (length > 0) {
			if (!StringUtils.isBlank(this.replace)) {
				String firstStr = value.substring(0, 1);
				value = value.replace(firstStr, this.replace);
				return value;
			} else {
				
				float f;
				try {
					f = Float.parseFloat(value);
					return String.format(this.format, f);
				} catch (NumberFormatException e) {
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public void init(IWorkbenchRTContext ctx, Map<String, Object> params) {
		if (params != null && params.containsKey("replace")) {
			this.replace = (String) params.get("replace");
		}
		
		if (params!=null && params.containsKey("format")) {
			this.format = (String) params.get("format");
		}
	}

	@Override
	public void destroy() {
		
	}

}
