/**
 * 
 */
package com.wxxr.mobile.core.ui.utils;

import java.util.Map;

import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValidationException;

/**
 * @author neillin
 *
 */
public class LongValueConvertor implements IValueConvertor<Long,String> {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueConvertor#valueOf(java.lang.String, com.wxxr.mobile.core.ui.api.IWorkbenchRTContext)
	 */
	public Long toSourceTypeValue(String input)
			throws ValidationException {
		if(input == null){
			return null;
		}
		try {
			return Long.valueOf(input);
		}catch(NumberFormatException e){
			throw new ValidationException("Invalid long value :"+input,e);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueConvertor#format(java.lang.Object, com.wxxr.mobile.core.ui.api.IWorkbenchRTContext)
	 */
	public String toTargetTypeValue(Long value) {
		if(value == null){
			return null;
		}
		return value.toString();
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
	public void init(IWorkbenchRTContext ctx, Map<String, Object> params) {
	}

	@Override
	public void destroy() {
	}

}
