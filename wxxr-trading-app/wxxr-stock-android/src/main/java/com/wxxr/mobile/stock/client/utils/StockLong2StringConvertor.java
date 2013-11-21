/**
 * 
 */
package com.wxxr.mobile.stock.client.utils;

import java.util.Map;

import android.text.format.Formatter;

import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValidationException;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author dz
 * 
 */
public class StockLong2StringConvertor implements IValueConvertor<Long, String> {

	private String format = "%+10.2f";
	private float multiple = 1.00f;

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
	}

	@Override
	public Long toSourceTypeValue(String s) throws ValidationException {
		if (StringUtils.isBlank(s)) {
			return null;
		}
		return (long) (Float.parseFloat(s) * multiple);
	}

	@Override
	public String toTargetTypeValue(Long val) {
		if (val == null) {
			return null;
		}
		return String.format(this.format, val / multiple);
	}

}
