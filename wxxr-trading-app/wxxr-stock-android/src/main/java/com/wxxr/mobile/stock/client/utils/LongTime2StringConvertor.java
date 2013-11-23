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
 * Long时间转字符串，根据自己传入的时间格式转换
 * 
 * @author dz
 **/
public class LongTime2StringConvertor implements IValueConvertor<Long, String> {

	private String format = "yyyy-MM-dd HH:mm:ss";

	private String append="";
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
		
		if (map.containsKey("append")) {
			this.append = (String) map.get("append");
		}
	}

	@Override
	public Long toSourceTypeValue(String s) throws ValidationException {
		if (StringUtils.isBlank(s)) {
			return null;
		}
		SimpleDateFormat sdf;
		try {
			sdf = new SimpleDateFormat(format);
		} catch (NullPointerException e) {
			throw new ValidationException("Invalid time-type value :" + s, e);
		} catch (IllegalArgumentException e) {
			throw new ValidationException("Invalid time-type value :" + s, e);
		}

		try {
			Date date = sdf.parse(s);
			return date.getTime();
		} catch (ParseException e) {
			throw new ValidationException("Invalid parse time value :" + s, e);
		}
	}

	@Override
	public String toTargetTypeValue(Long val) {
		if (val == null) {
			return null;
		}
		SimpleDateFormat sdf;
		try {
			sdf = new SimpleDateFormat(format);
		} catch (NullPointerException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
		Date date = new Date(val);
		return sdf.format(date) + append;
	}

}
