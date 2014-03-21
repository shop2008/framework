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
public class Float2PercentStringConvertor implements IValueConvertor<Float, String> {

	private String format = "%+10.2f";
	@Override
	public void destroy() {
	}

	@Override
	public Class<Float> getSourceType() {
		return Float.class;
	}

	@Override
	public Class<String> getTargetType() {
		return String.class;
	}

	@Override
	public void init(IWorkbenchRTContext ctx, Map<String, Object> map) {
		if(map.containsKey("format")){
			this.format = (String)map.get("format");
		}
	}

	@Override
	public Float toSourceTypeValue(String s) throws ValidationException {
		if(StringUtils.isBlank(s)){
			return null;
		}
		if (s.contains("%")) {
			int index = s.indexOf("%");
			if (index > 0)
				s = s.substring(0, index);
			return Float.parseFloat(s) / 100;
		}
		return Float.parseFloat(s);
	}

	@Override
	public String toTargetTypeValue(Float val) {
		if(val == null){
			return null;
		}
		return String.format(this.format, val * 100) + "%";
	}

}
