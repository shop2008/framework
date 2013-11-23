/**
 * 
 */
package com.wxxr.mobile.stock.client.utils;

import java.util.Formatter;
import java.util.Map;

import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValidationException;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public class Float2StringConvertor implements IValueConvertor<Float, String> {

	private String format = "%+10.2f";
	private String append = "";
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
		
		if (map.containsKey("append")) {
			this.append = (String)map.get("append");
		}
	}

	@Override
	public Float toSourceTypeValue(String s) throws ValidationException {
		if(StringUtils.isBlank(s)){
			return null;
		}
		return Float.parseFloat(s);
	}

	@Override
	public String toTargetTypeValue(Float val) {
		if(val == null){
			return null;
		}
		return String.format(this.format, val) + append;
	}

}
