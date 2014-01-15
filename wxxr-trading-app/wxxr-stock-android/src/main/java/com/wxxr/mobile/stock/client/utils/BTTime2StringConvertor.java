/**
 * 
 */
package com.wxxr.mobile.stock.client.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValidationException;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * BT时间字符串转格式化字符串，根据自己传入的时间格式转换
 * 
 * @author dz
 **/
public class BTTime2StringConvertor implements IValueConvertor<String, String> {

	private String BTformat = "yyyyMMddHHmmss";
	private String format = "yyyy-MM-dd HH:mm:ss";
	private String nullString;

	@Override
	public void destroy() {
	}

	@Override
	public Class<String> getSourceType() {
		return String.class;
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
		if (map.containsKey("nullString")) {
			this.nullString = (String) map.get("nullString");
		}
	}

	@Override
	public String toSourceTypeValue(String s) throws ValidationException {
		if (StringUtils.isBlank(s)) {
			return nullString;
		}
		SimpleDateFormat sdf, srcSdf;
		try {
			sdf = new SimpleDateFormat(format);
			srcSdf = new SimpleDateFormat(BTformat);
		} catch (NullPointerException e) {
			throw new ValidationException("Invalid time-type value :" + s);
		} catch (IllegalArgumentException e) {
			throw new ValidationException("Invalid time-type value :" + s);
		}

		try {
			Date date = sdf.parse(s);
			return srcSdf.format(date);
		} catch (ParseException e) {
			throw new ValidationException("Invalid parse time value :" + s);
		}
	}

	@Override
	public String toTargetTypeValue(String val) {
		if (StringUtils.isBlank(val)) {
			return nullString;
		}
		SimpleDateFormat sdf, tarSdf;
		try {
			sdf = new SimpleDateFormat(BTformat);
			tarSdf = new SimpleDateFormat(format);
		} catch (NullPointerException e) {
			return nullString;
		} catch (IllegalArgumentException e) {
			return nullString;
		}

		try {
			Date date = sdf.parse(val);
			return tarSdf.format(date);
		} catch (ParseException e) {
			return nullString;
		}
	}

}
