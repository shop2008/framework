/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IValueConvertor<T> {
	T valueOf(String input,IValueConvertorContext context) throws ValidationException;
	String format(T value, IValueConvertorContext context);
}
