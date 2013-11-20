/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Locale;

import com.wxxr.javax.validation.MessageInterpolator;

/**
 * @author neillin
 *
 */
public class DummyMessageInterpolator implements MessageInterpolator {

	/* (non-Javadoc)
	 * @see com.wxxr.javax.validation.MessageInterpolator#interpolate(java.lang.String, com.wxxr.javax.validation.MessageInterpolator.Context)
	 */
	@Override
	public String interpolate(String template, Context context) {
		return template;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.javax.validation.MessageInterpolator#interpolate(java.lang.String, com.wxxr.javax.validation.MessageInterpolator.Context, java.util.Locale)
	 */
	@Override
	public String interpolate(String template, Context context, Locale locale) {
		return template;
	}

}
