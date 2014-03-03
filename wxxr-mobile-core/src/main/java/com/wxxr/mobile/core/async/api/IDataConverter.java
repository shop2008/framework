/**
 * 
 */
package com.wxxr.mobile.core.async.api;

/**
 * @author neillin
 *
 */
public interface IDataConverter<S,T> {
	T convert(S value) throws NestedRuntimeException;
}
