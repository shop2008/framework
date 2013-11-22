/**
 * 
 */
package com.wxxr.mobile.android.validation;

import java.util.Locale;
import java.util.Map;

import com.wxxr.javax.validation.MessageInterpolator;
import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.core.i10n.api.IMessageI10NService;
import com.wxxr.mobile.core.i10n.api.IMessageI10NService.I10NContext;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;

/**
 * @author neillin
 *
 */
public class ValidationMessageInterpolator<T extends IAndroidAppContext> extends AbstractModule<T> implements MessageInterpolator{

	@Override
	protected void initServiceDependency() {
		addRequiredService(IMessageI10NService.class);
	}

	@Override
	protected void startService() {
		context.registerService(MessageInterpolator.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(MessageInterpolator.class, this);
	}

	@Override
	public String interpolate(String message, Context validationCtx) {
		return interpolateMessage( message, validationCtx.getConstraintDescriptor().getAttributes() );
	}

	@Override
	public String interpolate(String message, Context validationCtx, Locale locale) {
		return interpolateMessage( message, validationCtx.getConstraintDescriptor().getAttributes() );
	}
	
	private String interpolateMessage(String message, final Map<String, Object> annotationParameters) {
		return getService(IMessageI10NService.class).getMessage(message, new I10NContext() {			
			@Override
			public Map<String, Object> getParameters() {
				return annotationParameters;
			}
		});
	}
	
}
