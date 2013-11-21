/**
 * 
 */
package com.wxxr.mobile.stock.client.utils;

import java.util.Map;

import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValidationException;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author dz
 * 
 */
public class StockLong2StringAutoUnitConvertor implements
		IValueConvertor<Long, String> {

	private String format = "%10.2f";
	private static String UNIT_100M = "亿";
	private static String UNIT_10T = "万";

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
	}

	@Override
	public Long toSourceTypeValue(String s) throws ValidationException {
		if (StringUtils.isBlank(s)) {
			return null;
		}
		if (s.contains(UNIT_100M)) {
			int index = s.indexOf(UNIT_100M);
			if (index > 0)
				s = s.substring(0, index);
			return (long) (Float.parseFloat(s) * 10E8);
		} else if (s.contains(UNIT_10T)) {
			int index = s.indexOf(UNIT_10T);
			if (index > 0)
				s = s.substring(0, index);
			return (long) (Float.parseFloat(s) * 10E4);
		}
		return (long) (Float.parseFloat(s));
	}

	@Override
	public String toTargetTypeValue(Long val) {
		if (val == null) {
			return null;
		}
		double multiple = 1;
		String unit = "";
		if (val > 10E8) {
			multiple = 10E8;
			unit = UNIT_100M;
		} else if (val > 10E4) {
			multiple = 10E4;
			unit = UNIT_10T;
		}
		return String.format(this.format, val / multiple) + unit;
	}

}
