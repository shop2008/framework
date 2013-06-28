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
public class StringValueConvertor implements IValueConvertor<String> {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueConvertor#valueOf(java.lang.String, com.wxxr.mobile.core.ui.api.IUIManagementContext)
	 */
	public String valueOf(String input, IValueConvertorContext context)
			throws ValidationException {
		return input;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IValueConvertor#format(java.lang.Object, com.wxxr.mobile.core.ui.api.IUIManagementContext)
	 */
	public String format(String value, IValueConvertorContext context) {
		return value;
	}

}
