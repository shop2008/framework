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
 * Long转货币单位型字符串
 * 
 * format:必须float型格式化 formatUnit:需要添加的单位
 * 
 * * @author dz
 **/
public class StockLong2StringAutoUnitConvertor implements
		IValueConvertor<Long, String> {

	private String format = "%10.2f";
	private int multiple = 1;
	private String nullString;
	private Long nullLong;
	// private String formatDefault = "%d";
	private String formatUnit = "";

	private static String UNIT_100M = "亿";
	private static String UNIT_10T = "万";

	private static String UNIT_DEFAULT = "元";

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
		// if (map.containsKey("formatDefault")) {
		// this.formatDefault = (String) map.get("formatDefault");
		// }
		if (map.containsKey("formatUnit")) {
			this.formatUnit = (String) map.get("formatUnit");
		}
		if (map.containsKey("multiple")) {
			this.multiple = Integer.parseInt((String) map.get("multiple"));
		}
		if (map.containsKey("nullString")) {
			this.nullString = (String) map.get("nullString");
		}
		if (map.containsKey("nullLong")) {
			this.nullLong = (Long) map.get("nullLong");
		}
	}

	@Override
	public Long toSourceTypeValue(String s) throws ValidationException {
		if (StringUtils.isBlank(s)) {
			return nullLong;
		}
		try {
			if (s.contains(UNIT_100M)) {
				int index = s.indexOf(UNIT_100M);
				if (index > 0)
					s = s.substring(0, index);
				return (long) (Float.parseFloat(s) * 1E8) * multiple;
			} else if (s.contains(UNIT_10T)) {
				int index = s.indexOf(UNIT_10T);
				if (index > 0)
					s = s.substring(0, index);
				return (long) (Float.parseFloat(s) * 1E4) * multiple;
			} else if (s.contains(formatUnit)) {
				int index = s.indexOf(formatUnit);
				if (index > 0)
					s = s.substring(0, index);
				return (long) (Float.parseFloat(s)) * multiple;
			} else if (s.contains(UNIT_DEFAULT)) {
				int index = s.indexOf(UNIT_DEFAULT);
				if (index > 0)
					s = s.substring(0, index);
				return (long) (Float.parseFloat(s)) * multiple;
			}
			return (long) (Float.parseFloat(s)) * multiple;
		} catch (NumberFormatException e) {
			throw new ValidationException("Invalid Long value :" + s, e);
		}
	}

	@Override
	public String toTargetTypeValue(Long val) {
		if (val == null) {
			return nullString;
		}
		if(multiple == 0)
			return null;
		val = val / multiple;
		try {
			float multi = 1;
			String unit = "";
			if (val >= 1E8) {
				multi = (float) 1E8;
				unit = UNIT_100M;
			} else if (val >= 1E4) {
				multi = (float) 1E4;
				unit = UNIT_10T;
			} else {
				return String.format("%d", val)
						+ (StringUtils.isEmpty(this.formatUnit) ? UNIT_DEFAULT
								: formatUnit);
			}
			return String.format(this.format, val / multi) + unit
					+ (StringUtils.isEmpty(this.formatUnit) ? "" : formatUnit);
		} catch (NullPointerException e) {
			return null;
		} catch (IllegalFormatException e) {
			return null;
		}
	}

}
