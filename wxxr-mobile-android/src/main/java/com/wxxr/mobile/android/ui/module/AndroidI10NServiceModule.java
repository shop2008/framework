/**
 * 
 */
package com.wxxr.mobile.android.ui.module;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.android.ui.InvalidResourceIdException;
import com.wxxr.mobile.android.ui.RUtils;
import com.wxxr.mobile.core.i10n.api.IMessageI10NService;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;

/**
 * @author neillin
 *
 */
public class AndroidI10NServiceModule<T extends IAndroidAppContext> extends AbstractModule<T> implements
		IMessageI10NService {

	private static final Pattern MESSAGE_PARAMETER_PATTERN = Pattern.compile( "(\\{[^\\}]+?\\})" );

	private boolean cacheMessages = false;
	
	private final Map<String, String> resolvedMessages = new WeakHashMap<String, String>();
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.i10n.api.IMessageI10NService#getMessageTemplate(java.lang.String)
	 */
	@Override
	public String getMessageTemplate(String key) {
		try {
			return getAndroidString(key);
		}catch(InvalidResourceIdException e){
			return key;
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.i10n.api.IMessageI10NService#getMessage(java.lang.String, com.wxxr.mobile.core.i10n.api.IMessageI10NService.I10NContext)
	 */
	@Override
	public String getMessage(String key, I10NContext ctx) {
		return interpolateMessage(key,(ctx != null ? ctx.getParameters() : null));
	}

	@Override
	protected void initServiceDependency() {
		
	}
	
	private String getAndroidString(String key) throws InvalidResourceIdException {
		int resId = RUtils.getInstance().getResourceId(RUtils.CATEGORY_NAME_STRING, key.replace('.', '_'));
		return context.getApplication().getAndroidApplication().getResources().getString(resId);
	}


	@Override
	protected void startService() {
		context.registerService(IMessageI10NService.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(IMessageI10NService.class, this);
	}
	
	private String interpolateMessage(String msgKey, Map<String, Object> params) {
		String resolvedMessage = null;

		if ( cacheMessages ) {
			resolvedMessage = resolvedMessages.get( msgKey );
		}

		// if the message is not already in the cache we have to run step 1-3 of the message resolution 
		if ( resolvedMessage == null ) {

			String newMessage;
			resolvedMessage = msgKey;
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
					resolvedMessages.put( msgKey, resolvedMessage );
				}
			} while ( true );
		}

		// resolve annotation attributes (step 4)
		if(params != null){
			resolvedMessage = replaceParameters( resolvedMessage, params );
		}
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

	private String replaceParameters(String message, Map<String, Object> params) {
		Matcher matcher = MESSAGE_PARAMETER_PATTERN.matcher( message );
		StringBuffer sb = new StringBuffer();
		while ( matcher.find() ) {
			String resolvedParameterValue;
			String parameter = matcher.group( 1 );
			Object variable = params.get( removeCurlyBrace( parameter ) );
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
