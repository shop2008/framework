/**
 * 
 */
package com.wxxr.mobile.core.ui.utils;

import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IValueConvertorContext;
import com.wxxr.mobile.core.ui.api.ValidationException;

/**
 * @author neillin
 *
 */
public class BooleanValueConvertor implements IValueConvertor<Boolean> {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueConvertor#validate(java.lang.String, com.wxxr.mobile.core.ui.api.IUIManagementContext)
	 */
	public Boolean valueOf(String input, IValueConvertorContext context)
			throws ValidationException {
		if(input == null){
			return null;
		}
		if("T".equalsIgnoreCase(input)||"Y".equalsIgnoreCase(input)||"YES".equalsIgnoreCase(input)||"TRUE".equals(input)){
			return true;
		}
		if("F".equalsIgnoreCase(input)||"N".equalsIgnoreCase(input)||"NO".equalsIgnoreCase(input)||"FALSE".equals(input)){
			return false;
		}
		throw new ValidationException("Invalid boolean value :"+input);
	}

	public String format(Boolean value, IValueConvertorContext context) {
		return value != null ?  value.toString() : null;
	}

}
