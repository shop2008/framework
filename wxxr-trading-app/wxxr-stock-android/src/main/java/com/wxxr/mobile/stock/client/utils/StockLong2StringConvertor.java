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
	}

	@Override
	public Long toSourceTypeValue(String s) throws ValidationException {
		if (StringUtils.isBlank(s)) {
			return null;
		}
		if (s.contains(formatUnit)) {
			int index = s.indexOf(formatUnit);
			if (index > 0 && index <= s.length())
				s = s.substring(0, index);
		}
		return (long) (Float.parseFloat(s) * multiple);
	}

	@Override
	public String toTargetTypeValue(Long val) {
		if (val == null) {
			return null;
		}
		return String.format(this.format, val / multiple) + formatUnit;
	}

}
