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
public class IntegerValueConvertor implements IValueConvertor<Integer> {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueConvertor#valueOf(java.lang.String, com.wxxr.mobile.core.ui.api.IUIManagementContext)
	 */
	public Integer valueOf(String input, IValueConvertorContext context)
			throws ValidationException {
		if(input == null){
			return null;
		}
		try {
			return Integer.valueOf(input);
		}catch(NumberFormatException e){
			throw new ValidationException("Invalid integer value :"+input,e);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueConvertor#format(java.lang.Object, com.wxxr.mobile.core.ui.api.IUIManagementContext)
	 */
	public String format(Integer value, IValueConvertorContext context) {
		if(value == null){
			return null;
		}
		return value.toString();
	}

}
