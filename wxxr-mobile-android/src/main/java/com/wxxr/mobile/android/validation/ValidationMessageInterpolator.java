/**
 * 
 */
package com.wxxr.mobile.android.validation;

import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import com.wxxr.javax.validation.MessageInterpolator;
import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.android.ui.InvalidResourceIdException;
import com.wxxr.mobile.android.ui.RUtils;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;

/**
 * @author neillin
 *
 */
public class ValidationMessageInterpolator<T extends IAndroidAppContext> extends AbstractModule<T> implements MessageInterpolator{

	private static final Pattern MESSAGE_PARAMETER_PATTERN = Pattern.compile( "(\\{[^\\}]+?\\})" );

	private static class LocalisedMessage {
		private final String message;
		private final Locale locale;

		LocalisedMessage(String message, Locale locale) {
			this.message = message;
			this.locale = locale;
		}

		@Override
		public boolean equals(Object o) {
			if ( this == o ) {
				return true;
			}
			if ( o == null || getClass() != o.getClass() ) {
				return false;
			}

			LocalisedMessage that = ( LocalisedMessage ) o;

			if ( locale != null ? !locale.equals( that.locale ) : that.locale != null ) {
				return false;
			}
			if ( message != null ? !message.equals( that.message ) : that.message != null ) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			int result = message != null ? message.hashCode() : 0;
			result = 31 * result + ( locale != null ? locale.hashCode() : 0 );
			return result;
		}
	}
	
	private boolean cacheMessages = false;
	
	private final Map<LocalisedMessage, String> resolvedMessages = new WeakHashMap<LocalisedMessage, String>();


	@Override
	protected void initServiceDependency() {
		
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
	
	private String interpolateMessage(String message, Map<String, Object> annotationParameters) {
		LocalisedMessage localisedMessage = new LocalisedMessage( message, Locale.getDefault() );
		String resolvedMessage = null;

		if ( cacheMessages ) {
			resolvedMessage = resolvedMessages.get( localisedMessage );
		}

		// if the message is not already in the cache we have to run step 1-3 of the message resolution 
		if ( resolvedMessage == null ) {

			String newMessage;
			resolvedMessage = message;
			boolean evaluatedDefaultBundleOnce = false;
			do {
				// search the user bundle recursive (step1)
				newMessage = replaceVariables(
						resolvedMessage, true
				);

				// exit condition - we have at least tried to validate against the default bundle and there was no
				// further replacements
				if ( evaluatedDefaultBundleOnce
						&& !hasReplacementTakenPlace( newMessage, resolvedMessage ) ) {
					break;
				}

				// search the default bundle non recursive (step2)
				resolvedMessage = newMessage;
				evaluatedDefaultBundleOnce = true;
				if ( cacheMessages ) {
					resolvedMessages.put( localisedMessage, resolvedMessage );
				}
			} while ( true );
		}

		// resolve annotation attributes (step 4)
		resolvedMessage = replaceAnnotationAttributes( resolvedMessage, annotationParameters );

		// last but not least we have to take care of escaped literals
		resolvedMessage = resolvedMessage.replace( "\\{", "{" );
		resolvedMessage = resolvedMessage.replace( "\\}", "}" );
		resolvedMessage = resolvedMessage.replace( "\\\\", "\\" );
		return resolvedMessage;
	}
	
	private boolean hasReplacementTakenPlace(String origMessage, String newMessage) {
		return !origMessage.equals( newMessage );
	}

	private String replaceVariables(String message, boolean recurse) {
		Matcher matcher = MESSAGE_PARAMETER_PATTERN.matcher( message );
		StringBuffer sb = new StringBuffer();
		String resolvedParameterValue;
		while ( matcher.find() ) {
			String parameter = matcher.group( 1 );
			resolvedParameterValue = resolveParameter(
					parameter, recurse
			);

			matcher.appendReplacement( sb, escapeMetaCharacters( resolvedParameterValue ) );
		}
		matcher.appendTail( sb );
		return sb.toString();
	}
	
	private String getAndroidString(String key) throws InvalidResourceIdException {
		int resId = RUtils.getInstance().getResourceId(RUtils.CATEGORY_NAME_STRING, key);
		return context.getApplication().getAndroidApplication().getResources().getString(resId);
	}
	
	private String resolveParameter(String parameterName, boolean recurse) {
		String parameterValue;
		try {
				parameterValue = getAndroidString( removeCurlyBrace( parameterName ) );
				if ( recurse ) {
					parameterValue = replaceVariables( parameterValue,recurse );
				}
		}
		catch ( InvalidResourceIdException e ) {
			// return parameter itself
			parameterValue = parameterName;
		}
		return parameterValue;
	}

	private String removeCurlyBrace(String parameter) {
		return parameter.substring( 1, parameter.length() - 1 );
	}

	private String replaceAnnotationAttributes(String message, Map<String, Object> annotationParameters) {
		Matcher matcher = MESSAGE_PARAMETER_PATTERN.matcher( message );
		StringBuffer sb = new StringBuffer();
		while ( matcher.find() ) {
			String resolvedParameterValue;
			String parameter = matcher.group( 1 );
			Object variable = annotationParameters.get( removeCurlyBrace( parameter ) );
			if ( variable != null ) {
				resolvedParameterValue = escapeMetaCharacters( variable.toString() );
			}
			else {
				resolvedParameterValue = parameter;
			}
			matcher.appendReplacement( sb, resolvedParameterValue );
		}
		matcher.appendTail( sb );
		return sb.toString();
	}

	/**
	 * @param s The string in which to replace the meta characters '$' and '\'.
	 *
	 * @return A string where meta characters relevant for {@link Matcher#appendReplacement} are escaped.
	 */
	private String escapeMetaCharacters(String s) {
		String escapedString = s.replace( "\\", "\\\\" );
		escapedString = escapedString.replace( "$", "\\$" );
		return escapedString;
	}



	/**
	 * @return the cacheMessages
	 */
	public boolean isCacheMessages() {
		return cacheMessages;
	}

	/**
	 * @param cacheMessages the cacheMessages to set
	 */
	public void setCacheMessages(boolean cacheMessages) {
		this.cacheMessages = cacheMessages;
	}


}
