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
public class LongValueConvertor implements IValueConvertor<Long> {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueConvertor#valueOf(java.lang.String, com.wxxr.mobile.core.ui.api.IWorkbenchRTContext)
	 */
	public Long valueOf(String input, IValueConvertorContext context)
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
	public String format(Long value, IValueConvertorContext context) {
		if(value == null){
			return null;
		}
		return value.toString();
	}

}
