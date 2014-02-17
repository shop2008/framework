/**
 * 
 */
package com.wxxr.mobile.stock.client.utils;

import java.util.IllegalFormatException;
import java.util.Map;

import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValidationException;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * 倍数转换格式化
 * 
 * format:float格式化字符 multiple:缩小的倍数，float型 formatUnit:需要添加的单位
 * 
 * @author dz
 * 
 */
public class StockLong2StringConvertor implements IValueConvertor<Long, String> {

	private String format = "%+10.2f";
	private float multiple = 1.00f;
	private String formatUnit = "";
	private String nullString;
	private Long nullLong;
	
	private String plusString = "";
	@Override
	public void destroy() {
	}

	@Override
	public Class<Long> getSourceType() {
		return Long.class;
	}

	@Override
	public Class<String> getTargetType() {
		return String.class;
	}

	@Override
	public void init(IWorkbenchRTContext ctx, Map<String, Object> map) {
		if (map.containsKey("format")) {
			this.format = (String) map.get("format");
		}
		if (map.containsKey("multiple")) {
			this.multiple = Float.parseFloat((String) map.get("multiple"));
		}
		if (map.containsKey("formatUnit")) {
			this.formatUnit = (String) map.get("formatUnit");
		}
		if (map.containsKey("nullString")) {
			this.nullString = (String) map.get("nullString");
		}
		if (map.containsKey("nullLong")) {
			this.nullLong = (Long) map.get("nullLong");
		}
		
		if(map.containsKey("plusString")) {
			this.plusString = (String) map.get("plusString");
		}
	}

	@Override
	public Long toSourceTypeValue(String s) throws ValidationException {
		if (StringUtils.isBlank(s)) {
			return nullLong;
		}
		if (s.contains(formatUnit)) {
			int index = s.indexOf(formatUnit);
			if (index > 0 && index <= s.length())
				s = s.substring(0, index);
		}
		try {
			return (long) (Float.parseFloat(s) * multiple);
		} catch (NumberFormatException e) {
			throw new ValidationException("Invalid Long value :" + s);
		}
	}

	@Override
	public String toTargetTypeValue(Long val) {
		if (val == null) {
			return nullString;
		}
		
		if(val < 0) {
			this.plusString = "";
		}
		try {
			return plusString+String.format(this.format, val / multiple) + formatUnit;
		} catch (NullPointerException e) {
			return null;
		} catch (IllegalFormatException e) {
			return null;
		}
	}

}
