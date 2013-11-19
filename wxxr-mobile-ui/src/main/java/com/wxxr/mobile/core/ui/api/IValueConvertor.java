/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.Map;

/**
 * @author neillin
 *
 */
public interface IValueConvertor<S,T> {
	Class<S> getSourceType();
	Class<T> getTargetType();
	S toSourceTypeValue(T input) throws ValidationException;
	T toTargetTypeValue(S value);
	void init(IWorkbenchRTContext ctx, Map<String, Object> params);
	void destroy();
}
