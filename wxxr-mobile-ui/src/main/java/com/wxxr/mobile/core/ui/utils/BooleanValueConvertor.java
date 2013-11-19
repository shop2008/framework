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
public class BooleanValueConvertor implements IValueConvertor<Boolean,String> {

	@Override
	public void destroy() {
		
	}

	@Override
	public Class<Boolean> getSourceType() {
		return Boolean.class;
	}

	@Override
	public Class<String> getTargetType() {
		return String.class;
	}

	@Override
	public Boolean toSourceTypeValue(String input) throws ValidationException {
		if(input == null){
			return null;
		}
		if("T".equalsIgnoreCase(input)||"Y".equalsIgnoreCase(input)||"YES".equalsIgnoreCase(input)||"TRUE".equalsIgnoreCase(input)){
			return true;
		}
		if("F".equalsIgnoreCase(input)||"N".equalsIgnoreCase(input)||"NO".equalsIgnoreCase(input)||"FALSE".equalsIgnoreCase(input)){
			return false;
		}
		throw new ValidationException("Invalid boolean value :"+input);
	}

	@Override
	public String toTargetTypeValue(Boolean value) {
		return value != null ?  value.toString() : null;
	}

	@Override
	public void init(IWorkbenchRTContext ctx, Map<String, Object> params) {
		
	}

}
