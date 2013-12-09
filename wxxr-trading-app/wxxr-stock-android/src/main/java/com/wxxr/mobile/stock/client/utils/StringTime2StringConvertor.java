package com.wxxr.mobile.stock.client.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValidationException;

public class StringTime2StringConvertor implements IValueConvertor<String, String> {

	
	private String format = "M月d日";
	
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
		
		SimpleDateFormat sdf;
		float f;
		Long l;
		String s = null;
		try {
			
			f = Float.parseFloat(value);
			String ss = String.format("%.0f", f);
			
			l = Long.parseLong(ss);
			if (l != null) {
				sdf = new SimpleDateFormat(format);
				Date date = new Date(l);
				s = sdf.format(date);
			}
		} catch (NumberFormatException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
		
		return s;
	}

	@Override
	public void init(IWorkbenchRTContext ctx, Map<String, Object> params) {
	
		if (params != null&&params.containsKey("format")) {
			this.format = (String) params.get("format");
		}
	}

	@Override
	public void destroy() {
		
	}

}
